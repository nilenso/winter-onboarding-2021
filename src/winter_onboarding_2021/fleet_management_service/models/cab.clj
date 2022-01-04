(ns winter-onboarding-2021.fleet-management-service.models.cab
  (:require [winter-onboarding-2021.fleet-management-service.db.cab :as cab-db]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.utils :as utils])
  (:import [org.postgresql.util PSQLException]))

(defn get-by-id [id]
  (if (uuid? id)
    (cab-db/get-by-id id)
    {:error :id-not-uuid}))

(defn find-by-keys [key-map]
  (if (every? true?
              (map (partial contains?
                            #{:name :distance-travelled :licence-plate :id})
                   (keys key-map)))
    (cab-db/find-by-keys key-map)
    {:error :key-not-in-schema}))


(defn create [cab]
  (try (cab-db/create cab)
       (catch PSQLException e
         (if (re-find  #"already exists." (.getMessage e))
           {:error :licence-plate-already-exists}
           {:error :generic-error}))))

(defn select
  ([] (cab-db/select! 0 (config/get-page-size)))
  ([offset limit] (cab-db/select! offset limit)))

(defn cabs-count []
  (:count (first (cab-db/get-count))))

(defn update! [id cab]
  (if (uuid? id)
    (if (every? true?
                (map (partial contains?
                              #{:id :name :distance-travelled :licence-plate})
                     (keys cab)))
      (cab-db/update! id cab)
      {:error :key-not-in-schema})
    {:error :id-not-uuid}))

(defn delete-by-id [id]
  {:pre [(string? id)]}
  (cab-db/delete {:id (java.util.UUID/fromString id)}))

(defn get-by-licence-plate [licence-no]
  (first (cab-db/find-by-keys {:licence-plate licence-no})))

(defn get-by-id-or-licence-plate [val]
  (let [id (utils/string->uuid val)
        licence-plate val]
    (cab-db/get-by-id-or-licence-plate id licence-plate)))
