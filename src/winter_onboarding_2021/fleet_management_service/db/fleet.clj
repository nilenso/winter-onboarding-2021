(ns winter-onboarding-2021.fleet-management-service.db.fleet
  (:require [winter-onboarding-2021.fleet-management-service.db.core :as db]))

(defn create [fleet]
  (db/insert! :fleets fleet))
