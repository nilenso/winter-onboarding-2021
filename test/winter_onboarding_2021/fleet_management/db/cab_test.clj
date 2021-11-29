(ns winter-onboarding-2021.fleet-management.db.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.cab :as cab]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management.factories :as factories])
  (:import [org.postgresql.util PSQLException]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(defn vectors-contain-same-elements? [x y]
  (= (set x) (set y)))

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

(deftest list-cabs
  (testing "Shoudld return a list of cabs"
    (let [cabs-list (factories/create-cabs 3)]
      (doall (map cab/create cabs-list))
      (is (= cabs-list
             (map dissoc-cab (cab/select)))))))

(deftest pagination
  (let [cabs (factories/create-cabs 12)]
    (doall (map cab/create cabs))
    (testing "Should return last 2 cabs from a table of 12 cabs"
      (let [offset 10
            limit 2]
        (is (vectors-contain-same-elements?  (take limit (reverse cabs))
                                             (map dissoc-cab
                                                  (cab/select offset limit))))))
    (testing "Should return first 10 cabs from 12 cabs"
      (let [offset 0
            limit 10]
        (is (vectors-contain-same-elements?  (take limit cabs)
                                             (map dissoc-cab
                                                  (cab/select offset limit))))))))


