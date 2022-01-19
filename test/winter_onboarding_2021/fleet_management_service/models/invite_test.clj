(ns winter-onboarding-2021.fleet-management-service.models.invite-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.models.invite :as invite]
            [winter-onboarding-2021.fleet-management-service.factories :as factory]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management-service.error :as errors]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [clj-time.format :as f]
            [clj-time.core :as cljt]
            [clj-time.coerce :as sqltime]))

(defn to-utc [dt]
  (cljt/to-time-zone (cljt/from-time-zone (f/parse (f/formatters :date) dt) (cljt/default-time-zone)) cljt/utc))

(defn select-keys-from-invite [invite]
  (select-keys invite [:invites/created-by
                       :invites/role
                       :invites/usage-limit
                       :invites/valid-until]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-invite
  (testing "Should create an invite with valid-until in UTC"
    (let [db-user (factory/admin)
          invite  #:invites{:created-by (:users/id db-user)
                            :usage-limit 2
                            :valid-until (sqltime/to-sql-date (f/parse (f/formatters :date) "2022-10-13"))
                            :role "manager"}
          _ (invite/create invite)
          resp (db-core/find-by-keys! :invites {:invites/role "manager"})]
      (is (= (assoc-in invite [:invites/valid-until] (sqltime/to-sql-time (to-utc "2022-10-13")))
             (select-keys-from-invite (first resp))))))
  (testing "Should throw 'validation-failed' when keys are missing"
    (let [token (utils/rand-str 6)
          invite  #:invites{:token token
                            :usage-limit 2
                            :valid-until (sqltime/to-sql-date (f/parse (f/formatters :date) "2022-10-13"))
                            :role "manager"
                            :status "disabled"}
          resp (invite/create invite)]
      (is (= errors/validation-failed
             resp))))) 

(deftest get-invites-created-by-specific-user
    (let [db-user1 (factory/admin)
          invite1 (factory/invite-admin #:invites{:created-by (:users/id db-user1)})
          invite2 (factory/invite-manager #:invites{:created-by (:users/id db-user1)})
          _ (invite/create invite1)
          _ (invite/create invite2)
          resp (invite/find-by-keys {:invites/created-by (:users/id db-user1)})
          resp2 (invite/find-by-keys {:invites/created-by (:users/id (utils/uuid))})]
      (testing "Should return list of invites filtered using user-id"
        (is (= [invite1
                invite2]
               (mapv select-keys-from-invite resp))))
      (testing "Should return empty list for other user-id"
        (is (= []
               resp2)))
      (testing "Should return no-valid-keys error when wrong key is passed"
        (is (= errors/no-valid-keys
               (invite/find-by-keys {:invites/wrong-key "haha"}))))))
