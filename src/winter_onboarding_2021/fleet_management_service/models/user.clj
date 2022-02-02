(ns winter-onboarding-2021.fleet-management-service.models.user
  (:require [clojure.spec.alpha :as s]
            [crypto.password.bcrypt :as password]
            [winter-onboarding-2021.fleet-management-service.db.user :as user]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.error :as errors])
  (:import [org.postgresql.util PSQLException]))

(defn- valid-user-uuids? [uuids]
  (every? #(s/valid? :users/id %) uuids))

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

(defn members [org roles]
  (let [valid-org-key-map (utils/select-keys-from-spec org ::specs/organisations-all-attr)]
    (cond
      (not (every? #(s/valid? :users/role %) roles)) (assoc errors/validation-failed
                                                            :err-msg "Roles are not valid")
      (empty? valid-org-key-map) errors/no-valid-keys
      :else (user/members org roles))))

(defn users-in-org? [tx user-ids org]
  (let [valid-org-key-map
        (utils/select-keys-from-spec org ::specs/organisations-all-attr)]
    (cond
      (not (valid-user-uuids? user-ids)) errors/id-not-uuid
      (empty? valid-org-key-map) errors/no-valid-keys
      :else (= (count user-ids)
               (:count (first (user/users-in-org tx
                                                 user-ids
                                                 valid-org-key-map)))))))
