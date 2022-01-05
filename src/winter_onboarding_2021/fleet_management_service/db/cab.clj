(ns winter-onboarding-2021.fleet-management-service.db.cab
  (:require [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [honey.sql :as sql]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.error :as errors]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [honey.sql.helpers :as h :refer [select from limit offset order-by update set where]]))

(defn create [cab]
  (if (s/valid? ::specs/create-cab-form cab)
    (db/insert! :cabs cab)
    errors/validation-failed))

(defn select! [off lim]
  (if (s/valid? ::specs/pagination-params {:offset off :limit lim})
    (db/query! (sql/format (-> (select :id
                                       :name
                                       :licence-plate
                                       :distance-travelled
                                       :created-at
                                       :updated-at)
                               (from :cabs)
                               (limit lim)
                               (offset off)
                               (order-by :created-at))))
    errors/validation-failed))

(defn get-count []
  (db/query! (sql/format (-> (select [:%count.* :count])
                             (from :cabs)))))

(defn get-by-id [id]
  (if (uuid? id)
    (db/get-by-id! :cabs id)
    errors/id-not-uuid))

(defn find-by-keys [key-map]
  (if (utils/key-exists-in-schema key-map
                                  #{:id :name :distance-travelled :licence-plate})
    (db/find-by-keys! :cabs key-map)
    errors/key-not-in-schema))

(defn update! [id cab]
  (if (uuid? id)
    (if (s/valid? ::specs/update-cab-form cab)
      (db/query! (sql/format (-> (update :cabs)
                                 (set cab)
                                 (where [:= :id id]))))
      errors/validation-failed)
    (errors/id-not-uuid)))


(defn delete [where-params]
  (if (utils/key-exists-in-schema where-params
                                  #{:id :name :distance-travelled :licence-plate})
    (db/delete! :cabs where-params)
    errors/key-not-in-schema))

(defn get-by-id-or-licence-plate [id licence-plate]
  (first (db/query! (sql/format (-> (select :*)
                                    (from :cabs)
                                    (where :or [:= :id id] [:= :licence-plate licence-plate]))))))
