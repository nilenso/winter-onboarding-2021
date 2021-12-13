(ns winter-onboarding-2021.fleet-management-service.models.cab
  (:require [winter-onboarding-2021.fleet-management-service.db.cab :as cab-db]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))

(defn get-by-id [id]
  (cab-db/get-by-id id))

(defn find-by-keys [key-map]
  (cab-db/find-by-keys key-map))

(defn create [cab]
  (cab-db/create cab))

(defn select
  ([] (cab-db/select! 0 (config/get-page-size)))
  ([offset limit] (cab-db/select! offset limit)))

(defn cabs-count []
  (:count (first (cab-db/get-count))))

(defn update! [id cab]
  (cab-db/update! id cab))

(defn delete-by-id [id]
  {:pre [(string? id)]}
  (cab-db/delete {:id (java.util.UUID/fromString id)}))

(defn get-by-licence-plate [licence-no]
  (first (cab-db/find-by-keys {:licence-plate licence-no})))

(defn get-by-id-or-licence-plate [id-or-licence-no]
  (if-let [id (utils/string->uuid id-or-licence-no)]
    (get-by-id id)
    (get-by-licence-plate id-or-licence-no)))
