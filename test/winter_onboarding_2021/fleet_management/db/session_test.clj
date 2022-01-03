(ns winter-onboarding-2021.fleet-management.db.session-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.db.session :as db-session]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-models]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.config :as config]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest lookup
  (testing "Should return us user id associated with a given session-id"
    (let [user (factories/user)
          created-user (user-models/create user)
          user-id (:users/id created-user)
          session-id (utils/uuid)
          created-session (db-core/insert! :sessions {:id session-id
                                                      :user-id user-id
                                                      :expires-at (+ (System/currentTimeMillis) (config/get-timeout-period))})
          db-resp (db-session/lookup session-id)]
      (is (= created-session db-resp)))))

(deftest insert
  (testing "Should add a session in the sessions table"
    (let [user (factories/user)
          created-user (user-models/create user)
          user-id (:users/id created-user)
          session-id (utils/uuid)
          created-session (db-session/insert session-id user-id)]

      (is (= created-session
             (db-session/lookup session-id))))))

(deftest delete
  (testing "Should delete the session given a session id"
    (let [user (factories/user)
          created-user (user-models/create user)
          user-id (:users/id created-user)
          session-id (utils/uuid)
          _ (db-session/insert session-id user-id)]
      (db-session/delete session-id)
      (is (= nil (db-session/lookup session-id))))))

(deftest user-session
  (testing "Should give us details about the session & the user associated with it"
    (let [user (factories/user)
          created-user (user-models/create user)
          user-id (:users/id created-user)
          session-id (utils/uuid)
          created-session (db-session/insert session-id user-id)
          joined-user-session (first (db-session/user-session session-id))]
      (is (= (select-keys (merge created-user created-session)
                          [:users/id :users/name :users/role :users/email
                           :sessions/id :sessions/expires-at])
             joined-user-session)))))
