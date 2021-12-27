(ns winter-onboarding-2021.fleet-management-service.session
  (:require [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.db.session :as session-db]))

(defn new [user-id]
  (let [session-id (utils/uuid)]
    (session-db/insert session-id user-id)
    session-id))

(defn lookup [session-id]
  (session-db/lookup session-id))

(defn delete [session-id]
  (session-db/delete session-id))

(defn join-user-with-session [session-id]
  (session-db/join-user-with-session session-id))
