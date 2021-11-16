(ns winter-onboarding-2021.fleet-management-service.db
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

(def db-spec {:dbtype "postgresql"
              :dbname "fleet_management"
              :host   "localhost"
              :port   5432})

(defn connect-db []
  (let [ds (jdbc/get-datasource db-spec)]
    (println "Connected to database" ds)))
