(ns winter-onboarding-2021.fleet-management.models.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.models.cab :as cab]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures])
  (:import [org.postgresql.util PSQLException]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-cab
  (testing "Should add a cab"
    (let [created-cab (cab/create {:licence_plate "HR20X 6710"
                                   :name "Maruti Celerio"
                                   :distance_travelled 2333})]
      (is (= #:cabs{:licence_plate "HR20X 6710"
                    :name "Maruti Celerio"
                    :distance_travelled 2333}
             (dissoc created-cab
                     :cabs/id
                     :cabs/created_at
                     :cabs/updated_at)))))

  (testing "Should fail to create a cab because of invalid cab"
    (is (thrown-with-msg?
         PSQLException
         #"null value in column \"licence_plate\" of relation \"cabs\""
         (cab/create {:name "Kia"})))))
