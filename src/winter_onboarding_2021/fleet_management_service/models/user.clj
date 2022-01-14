(ns winter-onboarding-2021.fleet-management-service.models.user
  (:require [winter-onboarding-2021.fleet-management-service.db.user :as user]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [crypto.password.bcrypt :as password]
            [winter-onboarding-2021.fleet-management-service.error :as errors])
  (:import [org.postgresql.util PSQLException]))

(defn create [user]
  (let [user-with-valid-keys (utils/select-keys-from-spec user ::specs/users)]
    (if (empty? user-with-valid-keys)
      errors/no-valid-keys
      (try (user/create user-with-valid-keys)
           (catch PSQLException e
             (if (re-find  #"already exists." (.getMessage e))
               errors/email-id-already-exists
               errors/generic-error))))))

(defn find-by-keys [key-map]
  (let [valid-key-map (utils/select-keys-from-spec key-map ::specs/users-all-attr)]
    (if (empty? valid-key-map)
      errors/no-valid-keys
      (user/find-by-keys valid-key-map))))

(defn authenticate [{:users/keys [email password]}]
  (if-let [db-user (first (user/find-by-keys  {:email email}))]
    (if (password/check password (:users/password db-user))
      {:found? true :user (dissoc db-user :users/password)}
      {:found? true :user nil})
    {:found? false :user nil}))

(def add-to-org user/add-to-org)

(def members user/members)
