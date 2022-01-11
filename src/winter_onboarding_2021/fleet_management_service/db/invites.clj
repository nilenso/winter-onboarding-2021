(ns winter-onboarding-2021.fleet-management-service.db.invites
  (:require [winter-onboarding-2021.fleet-management-service.db.core :as db-core]))

(defn insert [invite]
  (db-core/insert! :invites invite))
