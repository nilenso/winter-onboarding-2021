(ns winter-onboarding-2021.fleet-management-service.db.invite-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.invite :as invites-db]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [clj-time.format :as f]
            [clj-time.core :as cljt]
            [clj-time.coerce :as sqltime]))

(defn to-utc [dt]
  (if (= (type dt) java.lang.String)
    (cljt/to-time-zone (cljt/from-time-zone (f/parse (f/formatters :date) dt) (cljt/default-time-zone)) cljt/utc)
    (cljt/to-time-zone (sqltime/from-sql-date dt) cljt/utc)))

(defn select-keys-from-invite [invite]
  (select-keys invite [:invites/token
                       :invites/created-by
                       :invites/role
                       :invites/is-active
                       :invites/usage-limit
                       :invites/valid-until]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-invite
  (testing "Should create an invite with valid-until in UTC Timestamp"
    (let [db-admin-user (factories/admin)
          token (utils/rand-str 6)
          db-invite (invites-db/create {:invites/token token
                                        :invites/created-by (:users/id db-admin-user)
                                        :invites/role "admin"
                                        :invites/is-active true
                                        :invites/usage-limit 1
                                        :invites/valid-until (sqltime/to-sql-date (f/parse (f/formatters :date) "2022-10-13"))})
          resp (db-core/find-by-keys! :invites {:token (:invites/token db-invite)})]
      (is (= {:invites/token token
              :invites/created-by (:users/id db-admin-user)
              :invites/role "admin"
              :invites/is-active true
              :invites/usage-limit 1}
             (dissoc (select-keys-from-invite (first resp)) :invites/valid-until)))
      (is (= (sqltime/to-sql-time (to-utc "2022-10-13"))
             (:invites/valid-until (first resp)))))))

(deftest get-invites-created-by-specific-user
  (let [db-user1 (factories/admin)
        invite1 (factories/invite-admin #:invites{:created-by (:users/id db-user1)
                                                  :token (utils/rand-str 8)
                                                  :is-active true})
        invite2 (factories/invite-manager #:invites{:created-by (:users/id db-user1)
                                                    :token (utils/rand-str 8)
                                                    :is-active true})
        _ (invites-db/create invite1)
        _ (invites-db/create invite2)
        resp (invites-db/find-by-keys {:invites/created-by (:users/id db-user1)})
        resp2 (invites-db/find-by-keys {:invites/created-by (:users/id (utils/uuid))})]
    (testing "Should return list of invites filtered using user-id"
      (is (= [invite1
              invite2]
             (mapv select-keys-from-invite resp))))
    (testing "Should return empty list for other user-id"
      (is (= []
             resp2)))))
