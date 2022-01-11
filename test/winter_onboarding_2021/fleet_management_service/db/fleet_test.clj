(ns winter-onboarding-2021.fleet-management-service.db.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.fleet :as fleet-db]
            [winter-onboarding-2021.fleet-management-service.db.user :as user-db]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management-service.error :as error]))

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
      (fleet-db/create fleet)
      (is (= #:fleets{:name "Azkaban Fleet 1"
                      :created-by user-id} (select-keys (first (db-core/query! ["select * from fleets;"]))
                                                        [:fleets/created-by :fleets/name])))))
  (testing "Should not create a fleet if create-by is not uuid"
    (let [user {:users/name "Hermione Granger"
                :users/role "admin"
                :users/email "hermione@hogwart.edu"
                :users/password "weasley&potter@123"}
          user-id (:users/id (user-db/create user))
          fleet {:fleets/name "Azkaban Fleet 1"
                 :fleets/created-by (str user-id)}]
      (is (= error/validation-failed (fleet-db/create fleet))))))
