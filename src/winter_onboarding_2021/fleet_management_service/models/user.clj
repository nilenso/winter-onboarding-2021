(ns winter-onboarding-2021.fleet-management-service.models.user
  (:require [winter-onboarding-2021.fleet-management-service.db.user :as user]
            [crypto.password.bcrypt :as password]))

(defn create [user]
  (user/create user))

(defn find-by-keys [key-map]
  (user/find-by-keys key-map))

(defn authenticate [{:keys [email password]}]
  (if-let [db-user (first (user/find-by-keys  {:email email}))]
    (if (password/check password (:users/password db-user))
      {:found? true :user (dissoc db-user :users/password)}
      {:found? true :user nil})
    {:found? false :user nil}))

(defn add-user-to-org [org user]
  (user/add-user-to-org org user))
