(ns winter-onboarding-2021.fleet-management-service.models.cab
  (:require [winter-onboarding-2021.fleet-management-service.db.cab :as cab-db]
            [winter-onboarding-2021.fleet-management-service.config :as config]))

(defn create [cab]
  (cab-db/create cab))

(defn select
  ([] (cab-db/select 0 (config/get-page-size)))
  ([offset limit] (cab-db/select offset limit)))

(defn cabs-count []
  (cab-db/cabs-count))
