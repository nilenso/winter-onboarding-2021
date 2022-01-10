(ns winter-onboarding-2021.fleet-management-service.db.fleet
  (:require [clojure.spec.alpha :as s]
            [honey.sql :as sql]
            [honey.sql.helpers :refer [select from limit offset order-by join where]]
            [winter-onboarding-2021.fleet-management-service.error :as error]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.specs :as spec]))

(defn create [fleet]
  (if (s/valid? ::spec/fleets fleet)
    (db/insert! :fleets fleet)
    (let [error-msg (s/explain-str ::spec/fleets fleet)]
      (assoc error/validation-failed :error-msg error-msg))))

(defn total []
  (db/query! (sql/format (-> (select [:%count.* :count])
                             (from :fleets)))))

(defn select-fleets-for-admin [admin-id off lim]
  (db/query! (sql/format (-> (select :id
                                     :name
                                     :created-by
                                     :created-at)
                             (from :fleets)
                             (join :user_fleet [:= :user_fleet.fleet_id :fleets.id])
                             (limit lim)
                             (offset off)
                             (where [:= :user_fleet.user_id admin-id])
                             (order-by :created-at)))))

(defn managers [fleet]
  (db/query!
   (sql/format (-> (select :id
                           :name
                           :role
                           :email
                           :created-at
                           :updated-at
                           :org-id)
                   (from [:users])
                   (where
                    [:and
                     [:in :users.id (-> (select :user_id)
                                        (from [:user_fleet])
                                        (where [:= :fleet_id (:fleets/id fleet)]))]
                     [:= :role "manager"]])
                   (order-by :created-at)))))
