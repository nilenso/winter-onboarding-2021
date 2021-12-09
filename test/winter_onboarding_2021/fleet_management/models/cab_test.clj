(ns winter-onboarding-2021.fleet-management.models.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.models.cab :as cab-model]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures])
  (:import [org.postgresql.util PSQLException]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-cab
  (testing "Should add a cab"
    ;; NOTE: the cab/create input needs to be auto kebab'ised, need to use honeysql for this
    (let [created-cab (cab-model/create {:licence_plate "HR20X 6710"
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
         (cab-model/create {:name "Kia"})))))

(deftest get-single-cab
  (testing "Should get details of a single cab with a certain ID"
    (let [cab (cab-model/create {:licence-plate "HR20X 9999"
                                 :name "Maruti Celerio 12"
                                 :distance-travelled 12221})]
      (is (= cab
             (cab-model/get-by-id (str (:cabs/id cab)))))))

  (testing "Should return nil if there is no row with a id"
    (is (= nil
           (cab-model/get-by-id (str (java.util.UUID/randomUUID)))))))

(deftest find-by-key
  (testing "Should find a row with a specific licence-plate"
    (let [sample-cab {:licence-plate "HR20X 9999"
                      :name "Maruti Celerio 12"
                      :distance-travelled 12221}
          created-cab (cab-model/create sample-cab)
          selected-cab (cab-model/find-by-keys (select-keys sample-cab [:distance-travelled]))]

      (is (= created-cab (first selected-cab))))))

(deftest update-cab
  (testing "Should update a cab with given id"
    (let [cab {:name "Maruti"
               :licence-plate "MHOR1234"
               :distance-travelled 123340}
          inserted-cab (cab-model/create cab)
          id (str (inserted-cab :cabs/id))
          new-cab {:name "Maruti"
                   :licence-plate "MHOR1234"
                   :distance-travelled 123500}]
      (cab-model/update! id new-cab)
      (is (= 123500 ((cab-model/get-by-id id) :cabs/distance-travelled))))))

(deftest delete-by-id
  (testing "Should delete a cab which has a specific id"
    (let [cab #:cabs{:name "Foo cab 1"
                     :licence-plate "KA23X 4567"
                     :distance-travelled 122772}
          db-cab (cab-model/create cab)]
      (cab-model/delete-by-id (str (:cabs/id db-cab)))
      (is (= nil (-> db-cab
                     :cabs/id
                     str
                     cab-model/get-by-id))))))

(deftest get-cab-by-licence-plate
  (testing "Should return a cab given a licence plate"
    (let [cab {:name "Maruti"
               :licence-plate "ODAH1234"
               :distance-travelled 123445}
          cab-id (:cabs/id (cab-model/create cab))
          cab-by-licence (first (cab-model/get-by-licence-plate (:licence-plate cab)))]
      (is (= #:cabs{:id cab-id
                    :name "Maruti"
                    :licence-plate "ODAH1234"
                    :distance-travelled 123445} (select-keys
                                                 cab-by-licence
                                                 [:cabs/id :cabs/name :cabs/licence-plate :cabs/distance-travelled])))
      (is (= cab-id (:cabs/id cab-by-licence))))))
