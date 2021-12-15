(ns winter-onboarding-2021.fleet-management-service.db.user
  (:require [winter-onboarding-2021.fleet-management-service.db.core :as db]))

(defn create [user]
  (db/insert! :users user))
