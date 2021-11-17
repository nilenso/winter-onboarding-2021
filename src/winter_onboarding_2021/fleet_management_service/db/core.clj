(ns winter-onboarding-2021.fleet-management-service.db.core
  (:require [next.jdbc :as jdbc]
            [mount.core :as mount :refer [defstate]]
            [next.jdbc.result-set :as rs]
            [winter-onboarding-2021.fleet-management-service.config :refer [config]]))

(defn connect-db []
  (let [ds (jdbc/get-datasource (:db-spec config))]
    (jdbc/get-connection ds)))

(defn disconnect-db [connection]
  (.close connection))

(defstate db-conn
  :start (connect-db)
  :stop (disconnect-db db-conn))

(defn run-dummy-query []
  (jdbc/execute! db-conn
                 ["select * from users limit 2"]
                 {:builder-fn rs/as-unqualified-lower-maps}))
