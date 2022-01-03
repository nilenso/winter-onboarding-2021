(ns winter-onboarding-2021.fleet-management.session-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.session :as session]
            [winter-onboarding-2021.fleet-management.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-models]))


(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest lookup
  (testing "Should return us user id associated with a given session-id"
    (let [user (factories/user)
          created-user (user-models/create user)
          user-id (:users/id created-user)
          session-id (session/new user-id)
          session-value (session/lookup session-id)]
      (is (= user-id (:sessions/user-id session-value))))))

(deftest new
  (testing "Should write a user id to the session store"
    (let [user (factories/user)
          created-user (user-models/create user)
          user-id (:users/id created-user)
          session-id (session/new user-id)
          session-value (session/lookup session-id)]
      (is (= user-id (:sessions/user-id session-value))))))

(deftest delete-session
  (testing "Should delete a specific session with provided session-id"
    (let [user (factories/user)
          created-user (user-models/create user)
          user-id (:users/id created-user)
          session-id (session/new user-id)]
      (session/delete session-id)
      (is (= nil
             (session/lookup session-id))))))

(deftest user-session
  (testing "Should give us details about the session & the user associated with it"
    (let [user (factories/user)
          created-user (user-models/create user)
          user-id (:users/id created-user)
          session-id (session/new user-id)
          created-session (session/lookup session-id)
          joined-user-session (first (session/user-session session-id))]
      (is (= (select-keys (merge created-user created-session)
                          [:users/id :users/name :users/role :users/email
                           :sessions/id :sessions/expires-at])
             joined-user-session)))))
