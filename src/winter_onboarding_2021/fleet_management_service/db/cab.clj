(ns winter-onboarding-2021.fleet-management-service.db.cab
  (:require [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [honey.sql :as sql]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.error :as errors]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [honey.sql.helpers :as h :refer [select from limit offset order-by update set where]]))

(defn create [cab]
  (if (s/valid? ::specs/cabs cab)
    (db/insert! :cabs cab)
    (let [error-msg (s/explain-str ::specs/cabs cab)]
      (assoc errors/validation-failed :error-msg error-msg))))

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
    (assoc errors/validation-failed
           :error-msg "offset or limit is negative")))

(defn get-count []
  (db/query! (sql/format (-> (select [:%count.* :count])
                             (from :cabs)))))

(defn get-by-id [id]
  (if (s/valid? :cabs/id id)
    (db/get-by-id! :cabs id)
    errors/id-not-uuid))

(defn find-by-keys [key-map]
  (let [valid-key-map (utils/select-keys-from-spec key-map
                                                   ::specs/cabs-all-attr)]
    (if (empty? valid-key-map)
      errors/no-valid-keys
      (db/find-by-keys! :cabs valid-key-map))))

(defn update! [id cab]
  (cond
    (not (s/valid? :cabs/id id)) errors/id-not-uuid
    (not (s/valid? ::specs/cabs-update-form cab)) (assoc errors/validation-failed
                                                         :error-msg
                                                         (s/explain-str ::specs/cabs-update-form cab))
    :else (db/query! (sql/format (-> (update :cabs)
                                     (set cab)
                                     (where [:= :id id]))))))

(defn delete [where-params]
  (let [valid-where-params (utils/select-keys-from-spec where-params ::specs/cabs-all-attr)]
    (if (empty? valid-where-params)
      errors/no-valid-keys
      (db/delete! :cabs {:cabs/id (:cabs/id valid-where-params)}))))

(defn get-by-id-or-licence-plate [id licence-plate]
  (first (db/query! (sql/format (-> (select :*)
                                    (from :cabs)
                                    (where :or [:= :id id]
                                           [:= :licence-plate licence-plate]))))))
