(ns winter-onboarding-2021.fleet-management-service.models.cab
  (:require [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.models.core :as m-core]
            [next.jdbc.sql :as sql]))

(defn create [cab]
  (m-core/insert! db/db-conn :cabs cab))

(defn get-by-id [id]
  (sql/get-by-id db/db-conn
                 :cabs
                 (java.util.UUID/fromString id)
                 m-core/sql-opts))

(defn find-by-keys [key-map]
  (sql/find-by-keys db/db-conn
                    :cabs
                    key-map
                    m-core/sql-opts))
