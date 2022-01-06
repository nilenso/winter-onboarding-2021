(ns winter-onboarding-2021.fleet-management-service.db.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.cab :as cab-db]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.models.cab :as models]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.error :as errors])
  (:import [org.postgresql.util PSQLException]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(defn vectors-contain-same-elements? [x y]
  (= (set x) (set y)))

(deftest created-cab
  (testing "Should create a cab"
    (testing "Should add a cab"
      (let [created-cab (cab-db/create {:name "Maruti Celerio"
                                        :licence-plate "HR20X6710"
                                        :distance-travelled 2333})]
        (is (= #:cabs{:name "Maruti Celerio"
                      :licence-plate "HR20X6710"
                      :distance-travelled 2333}
               (utils/dissoc-irrelevant-keys-from-cab created-cab)))))
    (testing "Should fail to create a cab because of invalid cab"
      (is (= errors/validation-failed (cab-db/create {:name "Kia"}))))
    (testing "Should throw an exception if cab with same licence-plate already exists"
      (is (thrown-with-msg? PSQLException
                            #"Detail: Key \(licence_plate\)=\(HR20X6710\) already exists."
                            (cab-db/create {:name "Maruti Alto"
                                            :licence-plate "HR20X6710"
                                            :distance-travelled 300021}))))))

(deftest list-cabs
  (testing "Should return a list of cabs"
    (let [cabs-list (factories/create-cabs 3)
          offset 0
          page-length 10]
      (doall (map cab-db/create cabs-list))
      (is (= cabs-list
             (utils/remove-namespace (map utils/dissoc-irrelevant-keys-from-cab (cab-db/select! offset page-length)))))))
  (testing "Should return a validation error when offset or page-length are negative"
    (let [cabs-list (factories/create-cabs 3)
          offset -1
          page-length -2]
      (doall (map cab-db/create cabs-list))
      (is (= errors/validation-failed
             (cab-db/select! offset page-length))))))

(deftest get-by-id
  (testing "Should return a cab given an id"
    (let [created-cab (cab-db/create {:name "Maruti Celerio"
                                      :licence-plate "HR20X6710"
                                      :distance-travelled 2333})]
      (is (= created-cab
           (cab-db/get-by-id (:cabs/id created-cab))))))
  (testing "Should throw an error if id is not valid"
    (let [created-cab (cab-db/create {:name "Maruti Celerio"
                                      :licence-plate "HR20X67102"
                                      :distance-travelled 2333})]
      (is (= errors/id-not-uuid
           (cab-db/get-by-id (str (:cabs/id created-cab))))))))

(deftest pagination
  (let [cabs (factories/create-cabs 12)]
    (doall (map cab-db/create cabs))
    (testing "Should return last 2 cabs from a table of 12 cabs"
      (let [offset 10
            limit 2]
        (is (vectors-contain-same-elements?  (take limit (reverse cabs))
                                             (utils/remove-namespace (map utils/dissoc-irrelevant-keys-from-cab
                                                                          (cab-db/select! offset limit)))))))
    (testing "Should return first 10 cabs from 12 cabs"
      (let [offset 0
            limit 10]
        (is (vectors-contain-same-elements?  (take limit cabs)
                                             (utils/remove-namespace (map utils/dissoc-irrelevant-keys-from-cab
                                                                          (cab-db/select! offset limit)))))))))

(deftest update-cab
  (testing "Should delete a cab with updated details"
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
  (testing "Should throw validation error if distance-travelled is not present"
    (let [cab {:name "Maruti"
               :licence-plate "MHOR1235"
               :distance-travelled 123340}
          inserted-cab (cab-db/create cab)
          id (inserted-cab :cabs/id)
          new-cab̦ {:name "Maruti"}]
      (is (= errors/validation-failed (cab-db/update! id new-cab̦)))))
  (testing "Should throw error id not uuid"
    (let [cab {:name "Maruti"
               :licence-plate "MHOR1236"
               :distance-travelled 123340}
          inserted-cab (cab-db/create cab)
          id (str (inserted-cab :cabs/id))
          new-cab̦ {:name "Hyundai"}]
      (is (= errors/id-not-uuid (cab-db/update! id new-cab̦))))))

(deftest deletion
  (testing "Should delete a cab with a specific ID"
    (let [cab {:name "Foo cab"
               :licence-plate "KA20X2345"
               :distance-travelled 122290}
          db-cab (models/create cab)]
      (cab-db/delete {:id (:cabs/id db-cab)})
      (is (= nil (-> db-cab
                     :cabs/id
                     models/get-by-id)))))
  (testing "Should return 0 when a cab with a specific ID is not found for deletion"
    (let [cab-id (java.util.UUID/randomUUID)
          output (cab-db/delete {:id cab-id})]
      (is (= 0 (:next.jdbc/update-count output))))))

(deftest get-by-id-or-licence-plate
  (testing "Should return a cab, given a licence plate"
    (let [cab {:name "Maruti"
               :licence-plate "MHOR1234"
               :distance-travelled 123340}
          inserted-cab (cab-db/create cab)
          cab-by-licence (cab-db/get-by-id-or-licence-plate nil (:licence-plate cab))]
      (is (= cab-by-licence inserted-cab))))
  (testing "Should return a cab, given an id"
    (let [cab {:name "Maruti"
               :licence-plate "MHOR1233"
               :distance-travelled 123340}
          inserted-cab (cab-db/create cab)
          id (:cabs/id inserted-cab)
          cab-by-id (cab-db/get-by-id-or-licence-plate id (str id))]
      (is (= cab-by-id inserted-cab))))
  (testing "Should return nil, given a non-exsistent id or licence"
    (let [id (java.util.UUID/randomUUID)
          cab-by-id (cab-db/get-by-id-or-licence-plate id (str id))]
      (is (= nil cab-by-id)))))

(deftest find-by-keys
  (testing "Should return list of cabs"
    (cab-db/create {:name "Maruti Celerio"
                    :licence-plate "HR20X6710"
                    :distance-travelled 2333})
    (is (= #:cabs{:name "Maruti Celerio"
                  :licence-plate "HR20X6710"
                  :distance-travelled 2333}
           (utils/dissoc-irrelevant-keys-from-cab
            (first (cab-db/find-by-keys {:name "Maruti Celerio"}))))))
  (testing "Should return key-not-exists-in-schema error"
    (cab-db/create {:name "Maruti Celerio"
                    :licence-plate "HR20X6710AS"
                    :distance-travelled 2333})
    (is (= errors/key-not-in-schema
           (cab-db/find-by-keys {:wrong-key "Maruti Celerio"})))))
