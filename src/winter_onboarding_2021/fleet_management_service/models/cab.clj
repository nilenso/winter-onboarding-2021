(ns winter-onboarding-2021.fleet-management-service.models.cab
  (:require [next.jdbc.sql :as sql]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]))

(defn create [cab]
  (sql/insert! db/db-conn :cabs cab))
