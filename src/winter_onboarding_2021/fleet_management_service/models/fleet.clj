(ns winter-onboarding-2021.fleet-management-service.models.fleet
  (:require [winter-onboarding-2021.fleet-management-service.db.fleet :as fleet-db]))

(defn create [fleet]
  (fleet-db/create fleet))
