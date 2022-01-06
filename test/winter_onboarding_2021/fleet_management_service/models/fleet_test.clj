(ns winter-onboarding-2021.fleet-management-service.models.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.models.fleet :as fleet-model]
            [winter-onboarding-2021.fleet-management-service.db.user :as user-db]
            [winter-onboarding-2021.fleet-management-service.error :as error]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.db.fleet-test :as fleet-test-db]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-fleet
  (testing "Should create a fleet"
    (let [user {:users/name "Hermione Granger"
                :users/role "admin"
                :users/email "hermione@hogwarts.edu"
                :users/password "weasley&potter@123"}
          user-id (:users/id (user-db/create user))
          fleet {:fleets/name "Azkaban Fleet 1"
                 :fleets/created-by user-id}]
      (is (= #:fleets{:name "Azkaban Fleet 1"
                      :created-by user-id} (select-keys (fleet-model/create fleet)
                                                        [:fleets/name :fleets/created-by])))))
  (testing "Should ignore un-related keys while creating a fleet"
    (let [user {:users/name "Hermione Granger"
                :users/role "admin"
                :users/email "hermione@hogwart.edu"
                :users/password "weasley&potter@123"}
          user-id (:users/id (user-db/create user))
          fleet {:fleets/name "Azkaban Fleet 1"
                 :fleets/place "Azkaban"
                 :fleets/created-by user-id}]
      (is (= #:fleets{:name "Azkaban Fleet 1"
                      :created-by user-id} (select-keys (fleet-model/create fleet)
                                                        [:fleets/name :fleets/created-by])))))
  (testing "Should not create a fleet if all keys are un-related"
    (let [user {:users/name "Ron Weasley"
                :users/role "admin"
                :users/email "ron@hogwart.edu"
                :users/password "hermionie&potter@123"}
          user-id (:users/id (user-db/create user))
          fleet {:fleets/place "Azkaban Fleet 1"
                 :fleets/admin (str user-id)}]
      (is (= error/no-valid-keys (fleet-model/create fleet))))))

(deftest list-fleet
  (testing "Should fetch the first 10 fleets"
    (let [{:keys [user fleets]} (fleet-test-db/seed-user-fleets-db 15)
          db-fleets (mapv fleet-test-db/dissoc-irrelevant-keys (fleet-model/select-fleets-for-admin (:users/id user) 0 10))]
      (is (= (map fleet-test-db/dissoc-irrelevant-keys (subvec fleets 0 10))
             db-fleets)))))
