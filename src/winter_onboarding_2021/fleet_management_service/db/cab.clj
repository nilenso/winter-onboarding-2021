(ns winter-onboarding-2021.fleet-management-service.db.cab
  (:require [next.jdbc.sql :as sql]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [next.jdbc.result-set :as rs]
            [camel-snake-kebab.core :as csk]
            [honeysql.core :as honeysql]))

(def sql-opts {:builder-fn rs/as-kebab-maps
               :column-fn  csk/->snake_case_string
               :table-fn   csk/->snake_case_string})

(defn create [cab]
  (sql/insert! db/db-conn :cabs cab sql-opts))

(defn sqlmap [offset limit]
  {:select [:name :licence-plate :distance-travelled :created-at :updated-at]
   :from   [:cabs]
   :limit (keyword (str limit))
   :offset (keyword (str offset))})

(defn select
  ([] (sql/query db/db-conn
                 (honeysql/format (sqlmap 0 10))
                 sql-opts))
  ([offset limit] (sql/query db/db-conn
                             (honeysql/format (sqlmap offset limit))
                             sql-opts)))

(defn cabs-count []
  (sql/query db/db-conn
                (honeysql/format {:select [:%count.*]
                                  :as [:count]
                                  :from   [:cabs]})
                sql-opts))
