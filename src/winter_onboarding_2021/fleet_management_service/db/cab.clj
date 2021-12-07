(ns winter-onboarding-2021.fleet-management-service.db.cab
  (:require [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [honeysql.core :as honeysql]))

(defn select-query [offset limit]
  {:select [:name
            :licence-plate
            :distance-travelled
            :created-at
            :updated-at]
   :from   [:cabs]
   :limit limit
   :offset offset
   :order-by [:created-at]})

(defn create [cabs]
  (db/insert! :cabs cabs))

(defn select [offset limit]
  (db/query! (honeysql/format (select-query offset limit))))

(defn get-count []
  (db/query! (honeysql/format {:select [:%count.*]
                               :as [:count]
                               :from [:cabs]})))

(defn get-by-id [id]
  {:pre [(uuid? id)]}
  (db/get-by-id! :cabs id))

(defn find-by-keys [key-map]
  (db/find-by-keys! :cabs key-map))
