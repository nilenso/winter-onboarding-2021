(ns winter-onboarding-2021.fleet-management.session-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.session :as session]))

(use-fixtures :each fixtures/clear-session-state)

(deftest read-session
  (testing "Should read the value of the session id from the session store"
    (let [user-id (java.util.UUID/randomUUID)
          session-id (java.util.UUID/randomUUID)
          _ (reset! session/all-sessions {session-id user-id})
          session-value (session/read-session session-id)]
      (is (= user-id
             session-value)))))

(deftest write-session
  (testing "Should write a user id to the session store"
    (let [session-id (java.util.UUID/randomUUID)
          user-id (java.util.UUID/randomUUID)
          _ (session/write-session session-id user-id)
          session-value (session/read-session session-id)]
      (is (= user-id session-value)))))

(deftest delete-session
    (testing "Should delete a specific session with provided session-id"
      (let [user-id (java.util.UUID/randomUUID)
            session-id (java.util.UUID/randomUUID)]
        (session/write-session session-id user-id)
        (session/delete-session session-id)
        (is (= nil
               (session/read-session session-id))))))
