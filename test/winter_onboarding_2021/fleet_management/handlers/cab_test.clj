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
      (is (= {:title (:cabs/name cab)
              :content content}
             (handlers/view-cab {:params {:id (str (:cabs/id cab))}}))))))

(deftest list-cabs-handler
  (testing "Should return a list of 2 rows of cabs"
    (with-redefs [config/get-page-size (constantly 2)]
      (let [cabs (factories/create-cabs 3)
            _ (doall (map models/create cabs))
            output (handlers/get-cabs {})]
        (is (= 2 (count (hf/hiccup-find [:tbody :tr] (:content output)))))
        (is (not-empty (hf/hiccup-find [:#cab-next-page] (:content output)))))))

  (testing "Should return a list of 10 rows of cabs with next Page link"
      (let [cabs (factories/create-cabs 12)
            _ (doall (map models/create cabs))
            output (handlers/get-cabs {})]
        (is (= 10 (count (hf/hiccup-find [:tbody :tr] (:content output)))))
        (is (= 1 (count (hf/hiccup-find [:#cab-next-page] (:content output)))))))

  (testing "Should return 5 rows of cabs in page number 2"
      (let [page-2-output (handlers/get-cabs {:params
                                              {:page "2"}})]
        (is (= 5 (count (hf/hiccup-find [:tbody :tr]
                                        (:content page-2-output)))))
        (is (= 0 (count (hf/hiccup-find [:#cab-next-page]
                                        (:content page-2-output)))))))

  (testing "Should return 5 colums for :name :distance-travelled :licence-plate 
            :created-at :updated-at"
      (let [output (handlers/get-cabs {})
            hiccup-text (hf/hiccup-text (:content output))]
        (is (= 5 (count (hf/hiccup-find [:thead :tr :th]
                                        (:content output)))))
        (is (str/includes? hiccup-text "Name"))
        (is (str/includes? hiccup-text "Distance travelled"))
        (is (str/includes? hiccup-text "Licence plate"))
        (is (str/includes? hiccup-text "Created at"))
        (is (str/includes? hiccup-text "Updated at")))))
