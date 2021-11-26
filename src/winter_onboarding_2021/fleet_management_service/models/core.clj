(ns winter-onboarding-2021.fleet-management-service.models.core
  (:require [next.jdbc.result-set :as result-set]
            [clojure.string :as str]
            [next.jdbc.sql :as sql]))

(defn as-kebab-maps [rs opts]
  (let [kebab #(str/replace % #"_" "-")]
    (result-set/as-modified-maps
     rs
     (assoc opts :qualifier-fn kebab :label-fn kebab))))

(def sql-opts {:builder-fn as-kebab-maps})

(defn insert! [connectable table-name attributes]
  (sql/insert! connectable
               table-name
               attributes
               sql-opts))
