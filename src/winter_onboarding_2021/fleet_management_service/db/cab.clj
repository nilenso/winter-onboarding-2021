(ns winter-onboarding-2021.fleet-management-service.db.cab
  (:require [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [honeysql.core :as honeysql]))

(defn cab-select-query [offset limit]
  {:select [:name :licence-plate :distance-travelled :created-at :updated-at]
   :from   [:cabs]
   :limit (keyword (str limit))
   :offset (keyword (str offset))})

(defn create [cabs]
  (db/insert! :cabs cabs))

(defn select [offset limit]
  (db/query (honeysql/format (cab-select-query offset limit))))

(defn cabs-count []
  (db/query (honeysql/format {:select [:%count.*]
                                  :as [:count]
                                  :from   [:cabs]})))

