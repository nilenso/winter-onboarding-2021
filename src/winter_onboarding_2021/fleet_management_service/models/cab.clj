(ns winter-onboarding-2021.fleet-management-service.models.cab
  (:require [winter-onboarding-2021.fleet-management-service.db.cab :as cab-db]))

(defn create [cab]
  (cab-db/create cab))

(defn select 
  ([] (cab-db/select 0 10))
  ([offset limit] (cab-db/select offset limit)))

(defn count []
  (cab-db/cabs-count))
