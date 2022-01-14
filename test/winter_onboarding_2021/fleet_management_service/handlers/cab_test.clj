(ns winter-onboarding-2021.fleet-management-service.handlers.cab-test
  (:require [hiccup-find.core :as hf]
            [clojure.string :as str]
            [clojure.test :refer [deftest is testing use-fixtures]]
            [hiccup.page :as hp]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.models.cab :as models]
            [winter-onboarding-2021.fleet-management-service.views.cab :as views]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as handlers]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))

(use-fixtures :each fixtures/clear-db)
(use-fixtures :once fixtures/config fixtures/db-connection)

(deftest add-cab
  (testing "POST /cabs/ endpoint with valid cab data, shoudld redirect to '/cabs/<<uuid of new cab>> "
    (let [response (handlers/create {:form-params
                                     {:name "Test cab"
                                      :licence-plate "KA20X1234"
                                      :distance-travelled "1223"}})
          cab (first (models/find-by-keys {:cabs/licence-plate "KA20X1234"}))]

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
             (handlers/create {:form-params invalid-cab}))))))

(deftest view-single-cab
  (testing "Should return 200 code with the HTML of the single cab details view"
    (let [cab (models/create {:cabs/name "Foo cab"
                              :cabs/licence-plate "FooLicencePlate"
                              :cabs/distance-travelled 19191})
          content (views/cab cab)]
      (is (= {:title (str "Cab - " (:cabs/name cab))
              :content content}
             (handlers/view-cab {:params {:slug (str (:cabs/id cab))}})))
      (is (= {:title (str "Cab - " (:cabs/name cab))
              :content content}
             (handlers/view-cab {:params {:slug (str (:cabs/licence-plate cab))}})))
      (is (= {:title "No cabs found"
              :content (views/cab-not-found)}
             (handlers/view-cab {:params {:slug (str (java.util.UUID/randomUUID))}}))))))

(deftest list-cabs-handler
  (testing "Should return a list of 2 rows of cabs"
    (with-redefs [config/get-page-size (constantly 2)]
      (let [cabs (factories/create-cabs 3)
            db-cabs (doall (map models/create cabs))
            first-cab (first db-cabs)
            second-cab (second db-cabs)
            output (:content (handlers/get-cabs {}))
            html-output (hp/html5 output)]

        (is (= 2 (count (hf/hiccup-find [:tbody :tr] output))))
        (is (= 2 (count (hf/hiccup-find [:tr :td :a] output))))

        (is (str/includes? html-output (:cabs/name first-cab)))
        (is (str/includes? html-output (str (:cabs/distance-travelled first-cab))))
        (is (str/includes? html-output (:cabs/licence-plate first-cab)))
        (is (str/includes? html-output (utils/format-date (:cabs/updated-at first-cab))))
        (is (str/includes? html-output (utils/format-date (:cabs/created-at first-cab))))

        (is (str/includes? html-output (:cabs/name second-cab)))
        (is (str/includes? html-output (str (:cabs/distance-travelled second-cab))))
        (is (str/includes? html-output (:cabs/licence-plate second-cab)))
        (is (str/includes? html-output (utils/format-date (:cabs/updated-at second-cab))))
        (is (str/includes? html-output (utils/format-date (:cabs/created-at second-cab))))

        (is (not-empty (hf/hiccup-find [:#cab-next-page] output))))))

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

(deftest update-cab
  (testing "Should redirect to /cabs:slug with success flash
            after successfull update of cab with given id and cab data"
    (let [cab {:cabs/name "Maruti Cab"
               :cabs/licence-plate "HR20A1234"
               :cabs/distance-travelled 12333}
          cab-id (str (:cabs/id (models/create cab)))
          new-cab {:name "Maruti Cab"
                   :distance-travelled "13000"}
          response (handlers/update-cab {:params {:slug cab-id}
                                         :form-params new-cab})]
      (is (= 302 (response :status)))
      (is (= {:success true
              :style-class "alert alert-success"
              :message "Cab updated successfully!"} (response :flash)))
      (is (= (str "/cabs/" cab-id)
             (get-in response [:headers "Location"])))
      (is (= {:cabs/name "Maruti Cab"
              :cabs/distance-travelled 13000}
             (select-keys (models/get-by-id (utils/string->uuid cab-id))
                          [:cabs/name :cabs/distance-travelled])))))

  (testing "Should redirect with an error message when update fails"
    (let [cab-id (:cabs/id (models/create {:cabs/name "Some name"
                                           :cabs/licence-plate "Licenseplate"
                                           :cabs/distance-travelled 123}))
          response (handlers/update-cab {:params {:slug (str cab-id)}
                                         :form-params {:foo "boo"}})]
      (is (= 302 (:status response)))
      (is (= true (get-in response [:flash :error])))
      (is (re-find #"(?i)could not update" (get-in response [:flash :message])))
      (is (= {:cabs/name "Some name"
              :cabs/distance-travelled 123}
             (select-keys (models/get-by-id cab-id)
                          [:cabs/name :cabs/distance-travelled]))))))

(deftest update-cab-form
  (testing "Should return the html form to update cabs"
    (let [cab {:cabs/name "Test cab"
               :cabs/licence-plate "KA20X1234"
               :cabs/distance-travelled 1223}
          cab-id (:cabs/id (models/create cab))]
      (is (= "Update cab KA20X1234"
             (:title (handlers/update-cab-view {:params
                                                {:slug (str cab-id)}})))))))

(deftest deletion
  (testing "Should redirect to cabs' list when delete succeeds"
    (let [cab {:cabs/name "Foo cab whatever"
               :cabs/licence-plate "KA20A3456"
               :cabs/distance-travelled 12233}
          db-cab (models/create cab)
          id (str (:cabs/id db-cab))
          handler-resp (handlers/delete {:params {:id id}})]
      (is (= {:flash {:success true
                      :style-class "alert alert-success"
                      :message "Cab deleted successfully"}
              :status 302
              :headers {"Location" "/cabs"}
              :body ""}
             handler-resp))))

  (testing "Should redirect to /cabs when delete fails"
    (let [missing-uuid (str (java.util.UUID/randomUUID))
          handler-response (handlers/delete {:params {:id missing-uuid}})]
      (is (= {:flash {:error true
                      :style-class "alert alert-danger"
                      :message "Cab does not exist"}
              :status 302
              :headers {"Location" "/cabs"}
              :body ""}
             handler-response)))))
