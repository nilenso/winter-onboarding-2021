(ns winter-onboarding-2021.fleet-management-service.db.healthcheck
  (:require [winter-onboarding-2021.fleet-management-service.db.core :as db]))

(defn healthcheck-query []
  (db/query! ["SELECT 1 AS check"]))
