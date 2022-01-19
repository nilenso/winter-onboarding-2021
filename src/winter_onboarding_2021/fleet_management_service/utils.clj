(ns winter-onboarding-2021.fleet-management-service.utils
  (:require [clojure.spec.alpha :as s]))

;;uuid
(defn uuid []
  (java.util.UUID/randomUUID))

(defn string->uuid [id]
  (try (java.util.UUID/fromString id)
       (catch Exception _ nil)))

;;keys
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

(defn hiccup-attrs [element]
  (when (map? (second element))
    (second element)))

(defn- namespace-key [ns k]
  (keyword (str (name ns) "/" (name k))))

(defn namespace-keys [ns m]
  (->> m
       (map (fn [[k v]] [(namespace-key ns k) v]))
       (into {})))

(defn select-keys-from-spec [data spec]
  (let [required-attr (nth (s/describe spec) 2)]
    (select-keys data required-attr)))

(defn format-date [date]
  (.format (java.text.SimpleDateFormat. "MM/dd/yyyy HH:mm") date))

(defn offset [page-size page-number]
  (* page-size (dec page-number)))
