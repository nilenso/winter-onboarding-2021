(ns winter-onboarding-2021.fleet-management-service.db.user
  (:require [honey.sql :as sql]
            [honey.sql.helpers :refer [update set where]]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]))

(defn create [user]
  (db/insert! :users user))

(defn find-by-keys [key-map]
  (db/find-by-keys! :users key-map))

(defn add-user-to-org [org user]
  (db/query! (sql/format (-> (update :users)
                             (set {:org-id (:organisations/id org)})
                             (where [:= :id (:users/id user)])))))
