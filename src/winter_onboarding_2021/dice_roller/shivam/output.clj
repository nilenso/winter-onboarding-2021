(ns winter-onboarding-2021.dice-roller.shivam.output
  (:require [clojure.string :as string]))

(defn stringify-literal [{:keys [type] :as literal}]
  {:pre [(= type :literal)]}
  nil)

(defn stringify-bin-op [{:keys [type] :as bin-op}]
  {:pre [(= type :binary-op)]}
  nil)

(defn stringify-set [{:keys [type] :as st}]
  {:pre [(= type :set)]}
  nil)

(defn stringify-dice [{:keys [type] :as dice}]
  {:pre [(= type :dice)]}
  nil)

(defn stringify-operation [{:keys [type] :as operation}]
  {:pre [(= type :set-operation)]}
  nil)

(defn stringify-selector [{:keys [type] :as selector}]
  {:pre [(= type :set-selector)]}
  nil)

(defn stringify-by-type [entity]
  (if (string? entity)
    entity
    nil))
