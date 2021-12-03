(ns winter-onboarding-2021.fleet-management.handlers.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as cab-handler]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [hiccup-find.core :as hf]
            [winter-onboarding-2021.fleet-management.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.models.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.config :as config]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest add-cab
  (testing "POST  /cabs/ endpoint with valid cab data, shoudld redirect to '/cabs/new?success=true' "
    (is (= {:status 302
            :flash {:success true
                    :style-class "alert alert-success"
                    :message "Cab added successfully!"}
            :headers {"Location" "/cabs/new"}
            :body ""}
           (cab-handler/create {:multipart-params {:name "Test cab"
                                           :licence_plate "KA20X1234"
                                           :distance_travelled "1223"}}))))

  (testing "POST /cabs/ endpoint with invalid cab data, should redirect to '/cabs/new?error=true"
    (let [invalid-cab {:name "Test cab"
                       :licence_plate "KA20X1234"}]
      (is (= {:status 302
              :flash {:error true
                      :style-class "alert alert-danger"
                      :message "Could not add cab, try again!"
                      :data invalid-cab}
              :headers {"Location" "/cabs/new"}
              :body ""}
             (cab-handler/create {:multipart-params invalid-cab}))))))

(deftest list-cabs-handler
  (testing "Should return a list of 3 rows of cabs"
    (let [cabs (factories/create-cabs 3)]
        (doall (map cab/create cabs))
      (with-redefs [config/get-page-size (constantly 2)]
        (is (= 2 (count (hf/hiccup-find [:tbody :tr] (cab-handler/get-cabs {})))))
        (is (not-empty (hf/hiccup-find [:a] (cab-handler/get-cabs {})))))))

  (testing "Should return a list of 10 rows of cabs with next Page link"
    (let [cabs (factories/create-cabs 12)]
      (doall (map cab/create cabs))
      (is (= 10 (count (hf/hiccup-find [:tbody :tr] (cab-handler/get-cabs {})))))
      (is (= 1 (count (hf/hiccup-find [:a] (cab-handler/get-cabs {})))))))

  (testing "Should return 8 rows of cabs in page number 2"
    (is (= 10 (count (hf/hiccup-find [:tbody :tr] (cab-handler/get-cabs {:query-params
                                                                    {:page "2"}})))))
    (is (= 0 (count (hf/hiccup-find [:a] (cab-handler/get-cabs {:query-params
                                                            {:page "2"}}))))))

  (testing "Should return 5 colums for :name :distance-travelled :licence-plate 
            :created-at :updated-at"
    (is (= 5 (count (hf/hiccup-find [:thead :tr :th] (cab-handler/get-cabs {})))))))
