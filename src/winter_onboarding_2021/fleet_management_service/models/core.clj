(ns winter-onboarding-2021.fleet-management-service.models.core
  (:require [next.jdbc.result-set :as result-set]
            [next.jdbc.sql :as sql]
            [camel-snake-kebab.core :as csk]))

(def sql-opts {:builder-fn result-set/as-kebab-maps
               :table-fn csk/->snake_case_string
               :column-fn csk/->snake_case_string})

(defn insert! [connectable table-name attributes]
  (sql/insert! connectable
               table-name
               attributes
               sql-opts))
