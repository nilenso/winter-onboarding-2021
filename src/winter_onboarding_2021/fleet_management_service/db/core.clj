(ns winter-onboarding-2021.fleet-management-service.db.core
  (:require [next.jdbc :as jdbc]
            [mount.core :as mount :refer [defstate]]
            [next.jdbc.result-set :as rs]
            [winter-onboarding-2021.fleet-management-service.config :refer [config]]
            [camel-snake-kebab.core :as csk]
            [next.jdbc.sql :as sql]))

(def sql-opts {:builder-fn rs/as-kebab-maps
               :table-fn csk/->snake_case_string
               :column-fn csk/->snake_case_string})

(defn connect-db []
  (let [ds (jdbc/get-datasource (:db-spec config))]
    (jdbc/get-connection ds)))

(defn disconnect-db [connection]
  (.close connection))

(defstate db-conn
  :start (connect-db)
  :stop (disconnect-db db-conn))

(defn insert!
  ([table-name attributes] (insert! db-conn
                                    table-name
                                    attributes))
  ([tx table-name attributes] (sql/insert! tx
                                           table-name
                                           attributes
                                           sql-opts)))

(defn query!
  ([sql-params] (query! db-conn sql-params))
  ([tx sql-params] (sql/query tx sql-params sql-opts)))

(defn get-by-id! [table id]
  (sql/get-by-id db-conn table id sql-opts))

(defn find-by-keys! [table key-map]
  (sql/find-by-keys db-conn table key-map sql-opts))

(defn delete! [table where-params]
  (sql/delete! db-conn table where-params sql-opts))
