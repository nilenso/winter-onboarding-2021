(ns winter-onboarding-2021.fleet-management-service.db.organisation-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-models]
            [winter-onboarding-2021.fleet-management-service.db.organisation :as org-db]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)


(deftest create
  (testing "Should create an organisation in DB"
    (let [admin (factories/admin)
          admin-id (:users/id (user-models/create admin))
          org (factories/organisation {:organisations/created-by admin-id})
          _ (org-db/create org)
          db-org (first (db-core/query! ["SELECT * FROM organisations;"]))]
      (is (= org (select-keys db-org
                         [:organisations/id :organisations/name
                          :organisations/created-by]))))))
