(ns winter-onboarding-2021.fleet-management-service.models.cab
  (:require [winter-onboarding-2021.fleet-management-service.db.cab :as cab-db]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.error :as errors])
  (:import [org.postgresql.util PSQLException]))

(defn get-by-id [id]
  (if (uuid? id)
    (cab-db/get-by-id id)
    errors/id-not-uuid))

(defn find-by-keys [key-map]
  (if (utils/key-exists-in-schema key-map
                                  #{:id :name :distance-travelled :licence-plate})
    (cab-db/find-by-keys key-map)
    errors/key-not-in-schema))

(defn create [cab]
  (try (cab-db/create cab)
       (catch PSQLException e
         (if (re-find  #"already exists." (.getMessage e))
           errors/license-plate-already-exists
           errors/generic-error))))

(defn select
  ([] (cab-db/select! 0 (config/get-page-size)))
  ([offset limit] (cab-db/select! offset limit)))

(defn cabs-count []
  (:count (first (cab-db/get-count))))

(defn update! [id cab]
  (if (uuid? id)
    (if (utils/key-exists-in-schema cab
                                    #{:id :name :distance-travelled :licence-plate})
      (cab-db/update! id cab)
      errors/key-not-in-schema)
    errors/id-not-uuid))

(defn delete-by-id [id]
  (if (uuid? id)
    (cab-db/delete {:id id})
    errors/id-not-uuid))

(defn get-by-licence-plate [licence-no]
  (first (cab-db/find-by-keys {:licence-plate licence-no})))

(defn get-by-id-or-licence-plate [val]
  (let [id (utils/string->uuid val)
        licence-plate val]
    (cab-db/get-by-id-or-licence-plate id licence-plate)))
