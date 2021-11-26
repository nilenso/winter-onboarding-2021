(ns winter-onboarding-2021.fleet-management-service.db.cab
  (:require [next.jdbc.sql :as sql]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [next.jdbc.result-set :as rs]
            [camel-snake-kebab.core :as csk]))

(def sql-opts {:builder-fn rs/as-kebab-maps
               :column-fn  csk/->snake_case_string
               :table-fn   csk/->snake_case_string})

(defn create [cab]
  (sql/insert! db/db-conn :cabs cab sql-opts))

(defn select []
  (sql/query db/db-conn ["SELECT * FROM cabs"] sql-opts))