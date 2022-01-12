(ns winter-onboarding-2021.fleet-management-service.models.cab
  (:require [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.db.cab :as cab-db]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.error :as errors])
  (:import [org.postgresql.util PSQLException]))

(defn get-by-id [id]
  (if (s/valid? :fleets/id id)
    (cab-db/get-by-id id)
    errors/id-not-uuid))

(defn find-by-keys [key-map]
  (let [valid-key-map (utils/select-keys-from-spec key-map
                                                   ::specs/cabs)]
    (if (empty? valid-key-map)
      errors/no-valid-keys
      (cab-db/find-by-keys key-map))))

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
  (let [cab-with-valid-keys (utils/select-keys-from-spec cab ::specs/cabs-update-form)]
    (cond (not (s/valid? :fleets/id id)) errors/id-not-uuid
          (empty? cab-with-valid-keys) errors/no-valid-keys
          :else (cab-db/update! id cab-with-valid-keys))))

(defn delete-by-id [id]
  (if (s/valid? :fleets/id id)
    (cab-db/delete {:cabs/id id})
    errors/id-not-uuid))

(defn get-by-licence-plate [licence-no]
  (first (cab-db/find-by-keys {:cabs/licence-plate licence-no})))

(defn get-by-id-or-licence-plate [val]
  (let [id (utils/string->uuid val)
        licence-plate val]
    (cab-db/get-by-id-or-licence-plate id licence-plate)))
