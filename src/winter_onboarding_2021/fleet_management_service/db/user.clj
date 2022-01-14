(ns winter-onboarding-2021.fleet-management-service.db.user
  (:require [honey.sql :as sql]
            [clojure.spec.alpha :as s]
            [honey.sql.helpers :refer [update set where]]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.error :as error]))

(defn create [user]
  (if (s/valid? ::specs/users user)
    (db/insert! :users user)
    (let [error-msg (s/explain-str ::specs/users user)]
      (assoc error/validation-failed :error-msg error-msg))))

(defn find-by-keys [key-map]
  (db/find-by-keys! :users key-map))

(defn add-to-org [tx org user]
  (db/query! tx (sql/format (-> (update :users)
                             (set {:org-id (:organisations/id org)})
                             (where [:= :id (:users/id user)])))))

(defn members [org role]
  (db/find-by-keys! :users {:role role
                            :org-id (:organisations/id org)}))
