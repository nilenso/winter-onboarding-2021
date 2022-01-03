(ns winter-onboarding-2021.fleet-management-service.models.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.models.cab :as cab-model]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.db.cab :as cab-db])
  (:import [org.postgresql.util PSQLException ServerErrorMessage]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-cab
  (testing "Should add a cab"
    (let [created-cab (cab-model/create {:name "Maruti Celerio"
                                         :licence-plate "HR20X6710"
                                         :distance-travelled 2333})]
      (is (= #:cabs{:name "Maruti Celerio"
                    :licence-plate "HR20X6710"
                    :distance-travelled 2333}
             (select-keys created-cab
                     [:cabs/name
                     :cabs/licence-plate
                     :cabs/distance-travelled])))))

  (testing "Should fail to create a cab because of invalid cab"
    (is (= {:error :validation-failed} (cab-model/create {:name "Kia"}))))
  (testing "Should fail if cab with same licence-plate already exists"
    (is (= {:error :licence-plate-already-exists}
           (cab-model/create {:name "Maruti Alto"
                              :licence-plate "HR20X6710"
                              :distance-travelled 300021}))))
  (testing "Should return generic-error if anything wrong happens while creating cab"
    (with-redefs [cab-db/create (fn [_] (throw (PSQLException.
                                                (ServerErrorMessage. "Something wrong happened"))))]
      (is (= {:error :generic-error}
             (cab-model/create {:name "Maruti Alto"
                                :licence-plate "HR20X6712"
                                :distance-travelled 300021}))))))

(deftest get-single-cab
  (testing "Should get details of a single cab with a certain ID"
    (let [cab (cab-model/create {:name "Maruti Celerio 12"
                                 :licence-plate "HR20X9999"
                                 :distance-travelled 12221})]
      (is (= cab
             (cab-model/get-by-id (:cabs/id cab))))))

  (testing "Should return nil if there is no row with a id"
    (is (= nil
           (cab-model/get-by-id (java.util.UUID/randomUUID))))))

(deftest find-by-key
  (testing "Should find a row with a specific licence-plate"
    (let [sample-cab {:licence-plate "HR20X9999"
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
          id (:cabs/id inserted-cab)
          new-cab {:name "Maruti"
                   :licence-plate "MHOR1234"
                   :distance-travelled 123500}]
      (cab-model/update! id new-cab)
      (is (= 123500 ((cab-model/get-by-id id) :cabs/distance-travelled))))))

(deftest delete-by-id
  (testing "Should delete a cab which has a specific id"
    (let [cab {:name "Foo cab 1"
               :licence-plate "KA23X4567"
               :distance-travelled 122772}
          db-cab (cab-model/create cab)]
      (cab-model/delete-by-id (str (:cabs/id db-cab)))
      (is (= nil (-> db-cab
                     :cabs/id
                     cab-model/get-by-id))))))

(deftest get-cab-by-licence-plate
  (testing "Should return a cab given a licence plate"
    (let [cab {:name "Maruti"
               :licence-plate "ODAH1234"
               :distance-travelled 123445}
          cab-id (:cabs/id (cab-model/create cab))
          cab-by-licence (cab-model/get-by-licence-plate (:licence-plate cab))]
      (is (= #:cabs{:id cab-id
                    :name "Maruti"
                    :licence-plate "ODAH1234"
                    :distance-travelled 123445} (select-keys
                                                 cab-by-licence
                                                 [:cabs/id :cabs/name :cabs/licence-plate :cabs/distance-travelled])))
      (is (= cab-id (:cabs/id cab-by-licence))))))

(deftest get-cab-by-licence-plate-or-id
  (testing "Should return a cab given a licence plate number or an id"
    (let [cab {:name "Maruti"
               :licence-plate "ODAH12342"
               :distance-travelled 123445}
          cab-id (:cabs/id (cab-model/create cab))
          cab-by-licence-or-id (cab-model/get-by-id-or-licence-plate (:licence-plate cab))]
      (is (= #:cabs{:id cab-id
                    :name "Maruti"
                    :licence-plate "ODAH12342"
                    :distance-travelled 123445}
             (select-keys cab-by-licence-or-id
                          [:cabs/id :cabs/name :cabs/licence-plate :cabs/distance-travelled])))
      (is (= cab-id (:cabs/id cab-by-licence-or-id)))))

  (testing "Should return a cab given a cab id"
    (let [cab {:name "Maruti"
               :licence-plate "ODAH1234"
               :distance-travelled 123445}
          cab-id (:cabs/id (cab-model/create cab))
          cab-by-licence (cab-model/get-by-id-or-licence-plate (str cab-id))]
      (is (= #:cabs{:id cab-id
                    :name "Maruti"
                    :licence-plate "ODAH1234"
                    :distance-travelled 123445}
             (select-keys cab-by-licence
                          [:cabs/id :cabs/name :cabs/licence-plate :cabs/distance-travelled])))
      (is (= cab-id (:cabs/id cab-by-licence)))))

  (testing "Should return nil if cab doesnt exist with a certain licence plate or UUID"
    (let [cab-by-licence (cab-model/get-by-id-or-licence-plate (str (java.util.UUID/randomUUID)))]
      (is (= nil cab-by-licence)))))
