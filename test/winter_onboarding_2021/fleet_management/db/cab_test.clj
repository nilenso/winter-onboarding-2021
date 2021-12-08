(ns winter-onboarding-2021.fleet-management.db.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.cab :as cab-db]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.models.cab :as models]))

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
             (map dissoc-irrelevant-keys (cab-db/select! offset page-length)))))))

(deftest pagination
  (let [cabs (factories/create-cabs 12)]
    (doall (map cab-db/create cabs))
    (testing "Should return last 2 cabs from a table of 12 cabs"
      (let [offset 10
            limit 2]
        (is (vectors-contain-same-elements?  (take limit (reverse cabs))
                                             (map dissoc-irrelevant-keys
                                                  (cab-db/select! offset limit))))))
    (testing "Should return first 10 cabs from 12 cabs"
      (let [offset 0
            limit 10]
        (is (vectors-contain-same-elements?  (take limit cabs)
                                             (map dissoc-irrelevant-keys
                                                  (cab-db/select! offset limit))))))))

(deftest update-cab
  (let [cab {:name "Maruti"
             :licence-plate "MHOR1234"
             :distance-travelled 123340}
        inserted-cab (cab-db/create cab)
        id (inserted-cab :cabs/id)
        new-cab̦ {:name "Maruti"
                  :licence-plate "MHOR1234"
                  :distance-travelled 123500}]
    (cab-db/update! id new-cab̦)
    (is (= 123500 ((cab-db/get-by-id id) :cabs/distance-travelled)))))

(deftest deletion
  (testing "Should delete a cab with a specific ID"
    (let [cab {:name "Foo cab"
               :licence-plate "KA20X 2345"
               :distance-travelled 122290}
          db-cab (models/create cab)]
      (cab-db/delete {:id (:cabs/id db-cab)})
      (is (= nil (-> db-cab
                     :cabs/id
                     str
                     models/get-by-id))))))
