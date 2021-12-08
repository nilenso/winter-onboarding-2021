(ns winter-onboarding-2021.fleet-management.handlers.cab-test
  (:require [hiccup-find.core :as hf]
            [clojure.string :as str]
            [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.models.cab :as models]
            [winter-onboarding-2021.fleet-management-service.views.cab :as views]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as handlers]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest add-cab
  (testing "POST /cabs/ endpoint with valid cab data, shoudld redirect to '/cabs/<<uuid of new cab>> "
    (let [response (handlers/create {:multipart-params
                                     {:name "Test cab"
                                      :licence-plate "KA20X1234"
                                      :distance-travelled "1223"}})
          cab (first (models/find-by-keys {:licence-plate "KA20X1234"}))]

      (is (= 302 (:status response)))

      (is (= {:success true
              :style-class "alert alert-success"
              :message "Cab added successfully!"}
             (:flash response)))

      (is (= (str "/cabs/" (:cabs/id cab))
             (get-in response [:headers "Location"])))

      (is (= "" (:body response)))))

  (testing "POST /cabs/ endpoint with invalid cab data, should redirect to '/cabs/new"
    (let [invalid-cab {:name "Test cab"
                       :licence-plate "KA20X1234"}]
      (is (= {:status 302
              :flash {:error true
                      :style-class "alert alert-danger"
                      :message "Could not add cab, try again!"
                      :data invalid-cab}
              :headers {"Location" "/cabs/new"}
              :body ""}
             (handlers/create {:multipart-params invalid-cab}))))))

(deftest view-single-cab
  (testing "Should return 200 code with the HTML of the single cab details view"
    (let [cab (models/create {:name "Foo cab"
                              :licence-plate "Foo Licence Plate"
                              :distance-travelled 19191})
          content (views/cab cab)]
      (is (= {:title (:name cab)
              :content content}
             (handlers/view-cab {:params {:id (str (:cabs/id cab))}}))))))

(deftest list-cabs-handler
  (testing "Should return a list of 3 rows of cabs"
    (let [cabs (factories/create-cabs 3)]
      (doall (map models/create cabs))
      (with-redefs [config/get-page-size (constantly 2)]
        (is (= 2 (count (hf/hiccup-find [:tbody :tr] (handlers/get-cabs {})))))
        (is (not-empty (hf/hiccup-find [:#cab-next-page] (handlers/get-cabs {})))))))

  (testing "Should return a list of 10 rows of cabs with next Page link"
    (let [cabs (factories/create-cabs 12)]
      (doall (map models/create cabs))
      (is (= 10 (count (hf/hiccup-find [:tbody :tr] (handlers/get-cabs {})))))
      (is (= 1 (count (hf/hiccup-find [:#cab-next-page] (handlers/get-cabs {})))))))

  (testing "Should return 8 rows of cabs in page number 2"
    (is (= 5 (count (hf/hiccup-find [:tbody :tr]
                                    (handlers/get-cabs
                                     {:params
                                      {:page "2"}})))))
    (is (= 0 (count (hf/hiccup-find [:#cab-next-page]
                                    (handlers/get-cabs
                                     {:params
                                      {:page "2"}}))))))

  (testing "Should return 5 colums for :name :distance-travelled :licence-plate
            :created-at :updated-at"
    (let [cabs-list (handlers/get-cabs {})
          hiccup-text (hf/hiccup-text cabs-list)]
      (is (= 5 (count (hf/hiccup-find [:thead :tr :th]
                                      cabs-list))))
      (is (str/includes? hiccup-text "name"))
      (is (str/includes? hiccup-text "distance-travelled"))
      (is (str/includes? hiccup-text "licence-plate"))
      (is (str/includes? hiccup-text "created-at"))
      (is (str/includes? hiccup-text "updated-at")))))

(deftest update-cab
  (testing "Should redirect to /cabs:id with success flash
            after successfull update of cab with given id and cab data"
    (let [cab {:name "Maruti Cab"
               :licence-plate "HR20A 1234"
               :distance-travelled 12333}
          cab-id (str (:cabs/id (models/create cab)))
          new-cab {:name "Maruti Cab"
                   :distance-travelled "13000"}
          response (handlers/update-cab {:params {:id cab-id}
                                         :multipart-params new-cab})]
      (is (= 302 (response :status)))
      (is (= {:success true
              :style-class "alert alert-success"
              :message "Cab updated successfully!"} (response :flash)))
      (is (= (str "/cabs/" cab-id)
             (get-in response [:headers "Location"])))
      (is (= {:cabs/name "Maruti Cab"
              :cabs/distance-travelled 13000}
             (select-keys (models/get-by-id cab-id)
                          [:cabs/name :cabs/distance-travelled])))))

  (testing "Should redirect with an error message when update fails"
    (let [cab-id (:cabs/id (models/create {:name "Some name"
                                           :licence-plate "License plate"
                                           :distance-travelled 123}))
          response (handlers/update-cab {:params {:id (str cab-id)}
                                         :multipart-params {:foo "boo"}})]
      (is (= 302 (:status response)))
      (is (= true (get-in response [:flash :error])))
      (is (re-find #"(?i)could not update" (get-in response [:flash :message])))
      (is (= {:cabs/name "Some name"
              :cabs/distance-travelled 123}
             (select-keys (models/get-by-id (str cab-id))
                          [:cabs/name :cabs/distance-travelled]))))))
