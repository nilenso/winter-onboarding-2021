(ns winter-onboarding-2021.fleet-management-service.db.session
  (:require [winter-onboarding-2021.fleet-management-service.db.core :as db-core]))

(defn lookup [session-id]
  (:sessions/user-id (db-core/get-by-id! :sessions session-id)))

(defn insert [session-id user-id]
  (db-core/insert! :sessions {:id session-id
                              :user-id user-id}))

(defn delete [session-id]
  (db-core/delete! :sessions {:id session-id}))
