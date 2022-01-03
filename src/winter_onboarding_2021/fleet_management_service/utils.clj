(ns winter-onboarding-2021.fleet-management-service.utils
  (:require [clojure.walk :refer [postwalk]]))

(defn string->uuid [id]
  (try (java.util.UUID/fromString id)
       (catch Exception _ nil)))

(defn dissoc-irrelevant-keys-from-user [user]
  (dissoc user
          :users/id
          :users/created-at
          :users/updated-at))

(defn uuid []
  (java.util.UUID/randomUUID))

(defn flash-msg [msg success?]
  (if success?
    {:flash {:success true
             :style-class "alert alert-success"
             :message msg}}
    {:flash {:error true
             :style-class "alert alert-danger"
             :message msg}}))

(defn dissoc-irrelevant-keys-from-cab [created-cab]
  (dissoc created-cab
          :cabs/id
          :cabs/created-at
          :cabs/updated-at))

(defn remove-namespace [coll]
  (postwalk
   (fn [key]
     (if (keyword? key)
       (keyword (name key))
       key))
   coll))
