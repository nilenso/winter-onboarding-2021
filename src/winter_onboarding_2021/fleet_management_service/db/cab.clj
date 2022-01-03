(ns winter-onboarding-2021.fleet-management-service.db.cab
  (:require [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [honey.sql :as sql]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [clojure.spec.alpha :as s]
            [honey.sql.helpers :as h :refer [select from limit offset order-by update set where]]))

(defn create [cab]
  (if (s/valid? ::specs/create-cab-form cab)
    (db/insert! :cabs cab)
    {:error :validation-failed}))

(defn select! [off lim]
  (db/query! (sql/format (-> (select :id
                                     :name
                                     :licence-plate
                                     :distance-travelled
                                     :created-at
                                     :updated-at)
                             (from :cabs)
                             (limit lim)
                             (offset off)
                             (order-by :created-at)))))

(defn get-count []
  (db/query! (sql/format (-> (select [:%count.* :count])
                             (from :cabs)))))

(defn get-by-id [id]
  {:pre [(uuid? id)]}
  (db/get-by-id! :cabs id))

(defn find-by-keys [key-map]
  (db/find-by-keys! :cabs key-map))

(defn update! [id cab]
  {:pre [(uuid? id)]}
  (db/query! (sql/format (-> (update :cabs)
                             (set cab)
                             (where [:= :id id])))))

(defn delete [where-params]
  (db/delete! :cabs where-params))

(defn get-by-id-or-licence-plate [id licence-plate]
  (first (db/query! (sql/format (-> (select :*)
                                    (from :cabs)
                                    (where :or [:= :id id] [:= :licence-plate licence-plate]))))))
