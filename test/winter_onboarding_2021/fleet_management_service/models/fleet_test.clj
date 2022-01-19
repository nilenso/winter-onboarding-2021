(ns winter-onboarding-2021.fleet-management-service.models.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.models.fleet :as fleet-model]
            [winter-onboarding-2021.fleet-management-service.error :as error]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.db.fleet-test :as fleet-test-db]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-fleet
  (testing "Should create a fleet"
    (let [user (factories/admin)
          user-id (:users/id user)
          fleet #:fleets{:name "Azkaban Fleet 1"
                         :created-by user-id}]
      (is (= fleet
             (select-keys (fleet-model/create fleet)
                          [:fleets/name :fleets/created-by])))))
  (testing "Should ignore un-related keys while creating a fleet"
    (let [user (factories/admin)
          user-id (:users/id user)
          fleet #:fleets{:name "Azkaban Fleet 1"
                         :place "Azkaban"
                         :created-by user-id}]
      (is (= #:fleets{:name "Azkaban Fleet 1"
                      :created-by user-id} (select-keys (fleet-model/create fleet)
                                                        [:fleets/name :fleets/created-by])))))
  (testing "Should not create a fleet if all keys are un-related"
    (let [user (factories/admin)
          user-id (:users/id user)
          fleet #:fleets{:place "Azkaban Fleet 1"
                         :admin (str user-id)}]
      (is (= error/no-valid-keys (fleet-model/create fleet))))))

(deftest total
  (testing "Should return the total count of fleets in the DB"
    (let [total 20
          _ (fleet-test-db/seed-user-fleets-db total)
          db-total (fleet-model/total)]
      (is (= total db-total)))))
