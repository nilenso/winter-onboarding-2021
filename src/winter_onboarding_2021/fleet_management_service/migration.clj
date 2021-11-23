(ns winter-onboarding-2021.fleet-management-service.migration
  (:require [winter-onboarding-2021.fleet-management-service.config :refer [config]]
            [mount.core :as mount]
            [migratus.core :as migratus]))

(def migratus-commands
  {:migrate migratus/migrate
   :rollback migratus/rollback
   :create migratus/create
   :up migratus/up
   :down migratus/down})

(defn run-migratus [command-name & args]
  (mount/start #'config)
  (let [command (keyword command-name)
        migration-config {:store :database
                          :db (:db-spec config)}
        migration-command (partial (migratus-commands command) migration-config)]
    (apply migration-command args)))