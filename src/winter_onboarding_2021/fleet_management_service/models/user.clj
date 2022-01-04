(ns winter-onboarding-2021.fleet-management-service.models.user
  (:require [winter-onboarding-2021.fleet-management-service.db.user :as user]
            [crypto.password.bcrypt :as password])
  (:import [org.postgresql.util PSQLException]))

(defn create [user]
  (if (every? true?
              (map (partial contains?
                            #{:name :role :id :email :password})
                   (keys user)))
    (try (user/create user)
         (catch PSQLException e
           (if (re-find  #"already exists." (.getMessage e))
             {:error :email-id-already-exists}
             {:error :generic-error})))
    {:error :key-not-in-schema}))

(defn find-by-keys [key-map]
  (if (every? true?
              (map (partial contains?
                            #{:name :role :id :email :password})
                   (keys key-map)))
    (user/find-by-keys key-map)
    {:error :key-not-in-schema}))

(defn authenticate [{:keys [email password]}]
  (if-let [db-user (first (user/find-by-keys  {:email email}))]
    (if (password/check password (:users/password db-user))
      {:found? true :user (dissoc db-user :users/password)}
      {:found? true :user nil})
    {:found? false :user nil}))
