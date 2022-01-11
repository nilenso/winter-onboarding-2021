(ns winter-onboarding-2021.fleet-management-service.db.invites-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.invites :as invites-db]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(factories/admin)

#_(deftest create-invite
  (testing "Should create an invite"
    (let [admin-user (factories/admin)
          _ (user-db)
          _ (invites-db/create {})])))
