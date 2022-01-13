(ns winter-onboarding-2021.fleet-management-service.handlers.organisation-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-models]
            [winter-onboarding-2021.fleet-management-service.handlers.organisation :as org-handlers]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create
  (testing "Should create an organisation and associate it with the logged in admin"
    (let [db-admin (factories/admin)
          request {:form-params {:name "org-1"} :user db-admin}
          response (org-handlers/create request)
          org (first (db-core/query! ["SELECT * FROM organisations;"]))
          user (first (user-models/find-by-keys {:users/id (:users/id db-admin)}))]
      (is (= 302 (:status response)))
      (is (= "/users/dashboard" (get-in response [:headers "Location"])))

      (is (= "org-1" (:organisations/name org)))
      (is (= (:organisations/id org) (:users/org-id user)))

      (is (= (:flash (utils/flash-msg "Organsiation created successfully" true))
             (:flash response)))))


  (testing "Should not create an organisation when the admin is already associated with an org"
    (let [db-admin (factories/admin)
          _ (org-handlers/create {:form-params {:name "foo-org"} :user db-admin})
          foo-org (first (db-core/find-by-keys! :organisations {:name "foo-org"}))
          response (org-handlers/create {:form-params {:name "foo-org-2"}
                                         :user (assoc db-admin
                                                      :users/org-id (:organisations/id foo-org))})
          org (db-core/find-by-keys! :organisations {:organisations/name "foo-org-2"})
          user (user-models/find-by-keys {:users/id (:users/id db-admin)})]

      (is (= 302 (:status response)))
      (is (= "/users/dashboard" (get-in response [:headers "Location"])))

      (is (= [] org))
      (is (= nil (:users/org-id user)))

      (is (= (:flash (utils/flash-msg "You are already associated with an organsation", false))
             (:flash response)))))


  (testing "Should not create an organisation and associate it with a user who is not an admin"
    (let [db-manager (factories/manager)
          request {:form-params {:name "org-2"} :user db-manager}
          response (org-handlers/create request)
          org (db-core/find-by-keys! :organisations {:organisations/name "org-2"})
          user (user-models/find-by-keys {:id (:users/id db-manager)})]

      (is (= 302 (:status response)))
      (is (= "/users/dashboard" (get-in response [:headers "Location"])))

      (is (= [] org))
      (is (= nil (:users/org-id user)))

      (is (= (:flash (utils/flash-msg "You need admin privileges to create an org", false))
             (:flash response))))))
