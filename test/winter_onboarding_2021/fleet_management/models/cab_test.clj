(ns winter-onboarding-2021.fleet-management.models.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.models.cab :as cab]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures])
  (:import [org.postgresql.util PSQLException]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-cab
  (testing "Should add a cab"
    ;; NOTE: the cab/create input needs to be auto kebab'ised, need to use honeysql for this
    (let [created-cab (cab/create {:licence_plate "HR20X 6710"
                                   :name "Maruti Celerio"
                                   :distance_travelled 2333})]
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

(deftest get-single-cab
  (testing "Should get details of a single cab with a certain ID"
    (let [cab (cab/create {:licence-plate "HR20X 9999"
                           :name "Maruti Celerio 12"
                           :distance-travelled 12221})]
      (is (= cab
             (cab/get-by-id (str (:cabs/id cab)))))))

  (testing "Should return nil if there is no row with a id"
    (is (= nil
           (cab/get-by-id (str (java.util.UUID/randomUUID)))))))

(deftest find-by-key
  (testing "Should find a row with a specific licence-plate"
    (let [sample-cab {:licence-plate "HR20X 9999"
                      :name "Maruti Celerio 12"
                      :distance-travelled 12221}
          created-cab (cab/create sample-cab)
          selected-cab (cab/find-by-keys (select-keys sample-cab [:distance-travelled]))]

      (is (= created-cab (first selected-cab))))))
