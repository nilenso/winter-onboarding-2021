(ns winter-onboarding-2021.fleet-management-service.session
  (:require [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.db.session :as session-db]))

(defn new [data]
  (let [session-id (utils/uuid)]
    (session-db/insert session-id data)
    session-id))

(defn lookup [session-id]
  (session-db/lookup session-id))

(defn delete [session-id]
  (session-db/delete session-id))
