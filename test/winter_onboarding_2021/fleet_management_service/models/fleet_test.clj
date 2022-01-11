(ns winter-onboarding-2021.fleet-management-service.models.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.models.fleet :as fleet-model]
            [winter-onboarding-2021.fleet-management-service.db.user :as user-db]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-fleet
  (testing "Should create a fleet"
    (let [user {:users/name "Hermione Granger"
                :users/role "admin"
                :users/email "hermione@hogwarts.edu"
                :users/password "weasley&potter@123"}
          user-id (:users/id (user-db/create user))
          fleet {:name "Azkaban Fleet 1"
                 :created-by user-id}]
      (is (= #:fleets{:name "Azkaban Fleet 1"
                      :created-by user-id} (select-keys (fleet-model/create fleet)
                                                        [:fleets/name :fleets/created-by]))))))
