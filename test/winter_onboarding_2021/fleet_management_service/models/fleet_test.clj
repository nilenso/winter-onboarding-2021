(ns winter-onboarding-2021.fleet-management-service.models.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.models.fleet :as fleet-model]
            [winter-onboarding-2021.fleet-management-service.error :as error]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.db.fleet-test :as fleet-test-db]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-fleet
  (testing "Should create a fleet"
    (let [{user-id :users/id} (factories/admin)
          {org-id :organisations/id} (factories/organisation {:organisations/created-by user-id})
          fleet #:fleets{:name "Azkaban Fleet 1"
                         :created-by user-id
                         :org-id org-id}]
      (is (= fleet
             (select-keys (fleet-model/create db-core/db-conn fleet)
                          [:fleets/name :fleets/created-by :fleets/org-id])))))
  (testing "Should ignore un-related keys while creating a fleet"
    (let [{user-id :users/id} (factories/admin)
          {org-id :organisations/id} (factories/organisation {:organisations/created-by user-id})
          fleet #:fleets{:name "Azkaban Fleet 1"
                         :place "Azkaban"
                         :created-by user-id
                         :org-id org-id}]
      (is (= #:fleets{:name "Azkaban Fleet 1"
                      :created-by user-id
                      :org-id org-id}
             (select-keys (fleet-model/create db-core/db-conn fleet)
                                                        [:fleets/name :fleets/created-by :fleets/org-id])))))
  (testing "Should not create a fleet if all keys are un-related"
    (let [user (factories/admin)
          user-id (:users/id user)
          fleet #:fleets{:place "Azkaban Fleet 1"
                         :admin (str user-id)}]
      (is (= error/no-valid-keys (fleet-model/create db-core/db-conn fleet))))))

(deftest total
  (testing "Should return the total count of fleets in the DB"
    (let [total 20
          _ (fleet-test-db/seed-user-fleets-db total)
          db-total (fleet-model/total)]
      (is (= total db-total)))))
