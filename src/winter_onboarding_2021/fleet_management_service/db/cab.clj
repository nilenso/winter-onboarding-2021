(ns winter-onboarding-2021.fleet-management-service.db.cab
  (:require [next.jdbc.sql :as sql]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [next.jdbc.result-set :as rs]))

(defn create [cab]
  (sql/insert! db/db-conn :cabs cab))


;;{:builder-fn rs/as-unqualified-lower-maps}

(defn select []
  (sql/query db/db-conn ["SELECT * FROM cabs"] {:builder-fn rs/as-kebab-maps}))
