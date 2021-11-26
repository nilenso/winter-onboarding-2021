(ns winter-onboarding-2021.fleet-management.handlers.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as cab]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest add-cab
  (testing "POST  /cabs/ endpoint with valid cab data, shoudld redirect to '/cabs/new?success=true' "
    (is {:status 302
         :headers {"Location" "/cabs/new?success=true"}}
        (cab/add {:multipart-params {:name "Test cab"
                                     :licence_plate "KA20X1234"
                                     :distance_travelled "1223"}})))
  
  (testing "POST /cabs/ endpoint with invalid cab data, should redirect to '/cabs/new?error=true"
    (is {:status 302
         :headers {"Location" "/cabs/new?error=true"}}
        (cab/add {:multipart-params {:name "Test cab"
                                     :licence_plate "KA20X1234"}}))))
