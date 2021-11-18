(ns winter-onboarding-2021.fleet-management-service.migration
  (:require [winter-onboarding-2021.fleet-management-service.config :refer [config]]
            [migratus.core :as migratus]))

(defn migration-config []
  {:store :database
   :db    (:db-spec config)})

(defn migrate []
  (migratus/migrate (migration-config)))

(defn rollback []
  (migratus/rollback (migration-config)))

(defn up [migration-id]
  (migratus/up (migration-config) migration-id))

(defn down [migration-id]
  (migratus/down (migration-config) migration-id))
