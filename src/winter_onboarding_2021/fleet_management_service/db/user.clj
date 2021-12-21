(ns winter-onboarding-2021.fleet-management-service.db.user
  (:require [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.error :as error]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [honey.sql :as sql]
            [honey.sql.helpers :as h :refer [select from where join]]))

(defn create [user]
  (if (s/valid? ::specs/users user)
    (db/insert! :users user)
    (let [error-msg (s/explain-str ::specs/users user)]
      (assoc error/validation-failed :error-msg error-msg))))

(defn find-by-keys [key-map]
  (db/find-by-keys! :users key-map))

;SELECT fleet_id from user_fleet, where user_id=user-id
;
;SELECT * FROM fleets INNER JOIN user_fleet ON (user_fleet.fleet_id = fleets.id);
;; SELECT * FROM fleets INNER JOIN user_fleet ON (user_fleet.fleet_id = fleets.id) 
;; where user_id='19c51a43-178a-4ea6-b7e7-a5c7c6a6cb66'; 

(defn get-fleets-by-user [user-id]
  (db/query! (sql/format (-> (select :fleet_id
                                     :name
                                     :created-at)
                             (from [:fleets])
                             (join :user_fleet [:= :user_fleet.fleet_id :fleets.id])
                             (where [:= :user-id user-id])))))
