(ns winter-onboarding-2021.fleet-management-service.db.organisation
  (:require [winter-onboarding-2021.fleet-management-service.db.core :as db]))

(defn create [organisation]
  (db/insert! :organisations organisation))
