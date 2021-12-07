(ns winter-onboarding-2021.fleet-management.db.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.cab :as cab-db]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management.factories :as factories]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(defn vectors-contain-same-elements? [x y]
  (= (set x) (set y)))


(defn dissoc-irrelevant-keys [created-cab]
  (dissoc created-cab
          :cabs/id
          :cabs/created-at
          :cabs/updated-at))

(deftest list-cabs
  (testing "Should return a list of cabs"
    (let [cabs-list (factories/create-cabs 3)
          offset 0
          page-length 10]
      (doall (map cab-db/create cabs-list))
      (is (= cabs-list
             (map dissoc-irrelevant-keys (cab-db/select offset page-length)))))))

(deftest pagination
  (let [cabs (factories/create-cabs 12)]
    (doall (map cab-db/create cabs))
    (testing "Should return last 2 cabs from a table of 12 cabs"
      (let [offset 10
            limit 2]
        (is (vectors-contain-same-elements?  (take limit (reverse cabs))
                                             (map dissoc-irrelevant-keys
                                                  (cab-db/select offset limit))))))
    (testing "Should return first 10 cabs from 12 cabs"
      (let [offset 0
            limit 10]
        (is (vectors-contain-same-elements?  (take limit cabs)
                                             (map dissoc-irrelevant-keys
                                                  (cab-db/select offset limit))))))))
