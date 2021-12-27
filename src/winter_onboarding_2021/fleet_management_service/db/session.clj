(ns winter-onboarding-2021.fleet-management-service.db.session
  (:require [honey.sql :as sql]
            [honey.sql.helpers :refer [join from where select]]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]

            [winter-onboarding-2021.fleet-management-service.config :as config]))

(defn lookup [session-id]
  (db-core/get-by-id! :sessions session-id))

(defn insert [session-id user-id]
  (db-core/insert! :sessions {:id session-id
                              :user-id user-id
                              :expires-at (+ (System/currentTimeMillis) (config/get-timeout-period))}))

(defn delete [session-id]
  (db-core/delete! :sessions {:id session-id}))

(defn join-user-with-session [session-id]
  (db-core/query! (sql/format
                   (-> (select  :users.id :users.name :users.role :users.email
                                :sessions.id :sessions.expires-at)
                       (from :sessions)
                       (join :users [:= :users.id :sessions.user_id])
                       (where [:= :sessions.id session-id])))))
