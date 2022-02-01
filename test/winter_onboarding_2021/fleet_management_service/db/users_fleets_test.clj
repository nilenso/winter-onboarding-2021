(ns winter-onboarding-2021.fleet-management-service.db.users-fleets-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.db.core :as core-db]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.db.users-fleets :as users-fleets-db]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create
  (testing "Should create an association between a fleet and a user"
    (let [{admin-id :users/id :as admin} (factories/admin)
          {org-id :organisations/id} (factories/organisation {:organisations/created-by admin-id})
          fleet (->> ::specs/fleets
                     (factories/overridden-generator {:fleets/created-by (:users/id admin)
                                                      :fleets/org-id org-id})
                     (factories/create :fleets))
          user_fleet_relation (users-fleets-db/create core-db/db-conn admin fleet)]

      (is (= user_fleet_relation
             (first (core-db/find-by-keys! :users-fleets
                                           {:user-id admin-id
                                            :fleet-id (:fleets/id fleet)})))))))
