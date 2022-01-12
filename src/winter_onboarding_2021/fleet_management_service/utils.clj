(ns winter-onboarding-2021.fleet-management-service.utils
  (:require [clojure.spec.alpha :as s]))

;;uuid
(defn uuid []
  (java.util.UUID/randomUUID))

(defn string->uuid [id]
  (try (java.util.UUID/fromString id)
       (catch Exception _ nil)))

;;keys
(defn dissoc-irrelevant-keys-from-user [user]
  (dissoc user
          :users/id
          :users/created-at
          :users/updated-at))

(defn dissoc-irrelevant-keys-from-cab [created-cab]
  (dissoc created-cab
          :cabs/id
          :cabs/created-at
          :cabs/updated-at))

;;flash-msgs
(defn flash-msg [msg success?]
  (if success?
    {:flash {:success true
             :style-class "alert alert-success"
             :message msg}}
    {:flash {:error true
             :style-class "alert alert-danger"
             :message msg}}))

(defn recursive-ns [ns hashmap map-keys]
  (if (empty? map-keys)
    hashmap
    (recursive-ns ns
                   (dissoc (assoc hashmap (keyword (str (name ns) "/" (name (first map-keys)))) ((first map-keys) hashmap))
                           (first map-keys))
                   (rest map-keys))))

(defn map->nsmap [hashmap ns]
  (recursive-ns ns hashmap (keys hashmap)))

(defn namespace-cabs [hashmap]
  (map->nsmap hashmap :cabs))

(defn namespace-users [hashmap]
  (map->nsmap hashmap :users))

(defn namespace-fleets [hashmap]
  (map->nsmap hashmap :fleets))

(defn select-keys-from-spec [data spec]
  (let [required-attr (nth (s/describe spec) 2)]
    (select-keys data required-attr)))
