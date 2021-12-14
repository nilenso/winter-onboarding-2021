(ns format-licence-plate
  (:require [mount.core :as mount]
            [honey.sql :as h-sql]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [honey.sql.helpers :as h]
            [clojure.spec.alpha :as s]
            [clojure.string :as string]
            [winter-onboarding-2021.fleet-management-service.specs]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]))

(defn process-cab
  "Will recieve a Cab hashmap & will return [cab-id new-licence-plate]
   where `new-licence-plate` only contains alphanumeric characters"
  [cab]
  (let [licence-plate (:cabs/licence-plate cab)]
    (when (s/invalid? (s/conform :cabs/licence-plate licence-plate))
      [(string/replace licence-plate #"[^a-zA-Z0-9]" "")
       (:cabs/id cab)])))

(defn run-data-migration []
  (try (mount/start)
       (let [cabs (sql/query db-core/db-conn
                             (h-sql/format (-> (h/select :name
                                                         :id
                                                         :licence_plate)
                                               (h/from :cabs)))
                             db-core/sql-opts)
             to-be-updated-cabs (remove nil? (map process-cab cabs))]
         (jdbc/execute-batch! db-core/db-conn
                              "UPDATE cabs SET licence_plate = ? WHERE id = ?"
                              to-be-updated-cabs
                              {})
         (println "Data migrated successfully"))
       (catch Exception e
         (println "Some error occured while migrating data")
         (println (.getMessage e)))))
