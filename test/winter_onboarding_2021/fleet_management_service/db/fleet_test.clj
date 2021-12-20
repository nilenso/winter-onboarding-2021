(ns winter-onboarding-2021.fleet-management-service.db.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.fleet :as fleet-db]
            [winter-onboarding-2021.fleet-management-service.db.user :as user-db]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-fleet
  (testing "Should create a fleet"
    (let [user {:name "Hermione Granger"
                :role "admin"
                :email "hermione@hogwarts.edu"
                :password "weasley&potter@123"}
          user-id (:users/id (user-db/create user))
          fleet {:name "Azkaban Fleet 1"
                 :created-by user-id}]
      (is (= #:fleets{:name "Azkaban Fleet 1"
                      :created-by user-id} (select-keys (fleet-db/create fleet)
                                                        [:fleets/name :fleets/created-by]))))))
