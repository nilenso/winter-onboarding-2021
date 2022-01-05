(ns winter-onboarding-2021.fleet-management-service.models.user
  (:require [winter-onboarding-2021.fleet-management-service.db.user :as user]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [crypto.password.bcrypt :as password]
            [winter-onboarding-2021.fleet-management-service.error :as errors])
  (:import [org.postgresql.util PSQLException]))

(defn create [user]
  (if (utils/key-exists-in-schema user
                                  #{:name :role :id :email :password})
    (try (user/create user)
         (catch PSQLException e
           (if (re-find  #"already exists." (.getMessage e))
             errors/email-id-already-exists
             errors/generic-error)))
    errors/key-not-in-schema))

(defn find-by-keys [key-map]
  (if (utils/key-exists-in-schema key-map
                                  #{:name :role :id :email :password})
    (user/find-by-keys key-map)
    errors/key-not-in-schema))

(defn authenticate [{:keys [email password]}]
  (if-let [db-user (first (user/find-by-keys  {:email email}))]
    (if (password/check password (:users/password db-user))
      {:found? true :user (dissoc db-user :users/password)}
      {:found? true :user nil})
    {:found? false :user nil}))
    