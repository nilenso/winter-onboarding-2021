(ns winter-onboarding-2021.fleet-management-service.models.organisation-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-models]
            [winter-onboarding-2021.fleet-management-service.models.organisation :as org-models]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create
  (testing "Should create an organisation in DB"
    (let [admin (factories/admin)
          admin-id (:users/id admin)
          org (gen/generate (gen/fmap #(assoc % :organisations/created-by admin-id)
                                      (s/gen ::specs/organisations)))
          _ (org-models/create org)
          db-org (first (db-core/query! ["SELECT * FROM organisations;"]))]
      (is (= org (select-keys db-org
                              [:organisations/id :organisations/name
                               :organisations/created-by]))))))

(deftest create-and-associate
  (testing "Should create an organisation and associate the given user with it"
    (let [admin (factories/admin)
          _ (org-models/create-and-associate {:organisations/name "foo-org"}
                                             admin)
          org (first (db-core/find-by-keys! :organisations {:organisations/name "foo-org"}))
          user (first (user-models/find-by-keys {:users/id (:users/id admin)}))]

      (is (= "foo-org" (:organisations/name org)))
      (is (= (:organisations/id org) (:users/org-id user))))))
