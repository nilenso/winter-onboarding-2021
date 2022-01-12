(ns winter-onboarding-2021.fleet-management-service.models.organisation
  (:require [winter-onboarding-2021.fleet-management-service.db.organisation :as org-db]))

(defn create [org]
  (org-db/create org))
