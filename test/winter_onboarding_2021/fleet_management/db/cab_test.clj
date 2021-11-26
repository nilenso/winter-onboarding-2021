(ns winter-onboarding-2021.fleet-management.db.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.cab :as cab]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management.factories :as factories])
  (:import [org.postgresql.util PSQLException]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-cab
  (testing "Should add a cab"
    (let [created-cab (cab/create {:licence_plate "HR20X 6710"
                                   :name "Maruti Celerio"
                                   :distance-travelled 2333})]
      (is (= #:cabs{:licence-plate "HR20X 6710"
                    :name "Maruti Celerio"
                    :distance-travelled 2333}
             (dissoc created-cab
                     :cabs/id
                     :cabs/created-at
                     :cabs/updated-at)))))

  (testing "Should fail to create a cab because of invalid cab"
    (is (thrown-with-msg?
         PSQLException
         #"null value in column \"licence_plate\" of relation \"cabs\""
         (cab/create {:name "Kia"})))))

(defn dissoc-cab [created-cab]
  (dissoc created-cab
          :cabs/id
          :cabs/created-at
          :cabs/updated-at))

(deftest cab-handler-test
  (testing "returns redered cabs list"))

(deftest list-cabs
  (testing "Returns a list of cabs"
    (let [cabs-list (factories/create-cabs 3)]
      (doall (map cab/create cabs-list))
      (is (= cabs-list
             (map dissoc-cab (cab/select)))))))