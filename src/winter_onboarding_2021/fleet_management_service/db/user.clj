(ns winter-onboarding-2021.fleet-management-service.db.user
  (:require [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.error :as error]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]))

(defn create [user]
  (if (s/valid? ::specs/users user)
    (db/insert! :users user)
    (let [error-msg (s/explain-str ::specs/users user)]
      (assoc error/validation-failed :error-msg error-msg))))

(defn find-by-keys [key-map]
  (db/find-by-keys! :users key-map))

(defn add-to-org [tx org user]
  (db/query! tx (sql/format (-> (h/update :users)
                                (h/set {:org-id (:organisations/id org)})
                                (h/where [:= :id (:users/id user)])))))

(defn members [org roles]
  (db/query! (sql/format (-> (h/select :*)
                             (h/from :users)
                             (h/where [:and [:in :role roles]
                                       [:= :org-id (:organisations/id org)]])))))

(defn users-in-org [tx user-ids org]
  (db/query! tx (sql/format (-> (h/select [:%count.* :count])
                                (h/from :users)
                                (h/where [:and
                                          [:in :id user-ids]
                                          [:= :org-id (:organisations/id org)]])))))
