(ns winter-onboarding-2021.fleet-management-service.utils)

(defn string->uuid [id]
  (try (java.util.UUID/fromString id)
       (catch Exception _ nil)))

(defn dissoc-irrelevant-keys-from-user [user]
  (dissoc user
          :users/id
          :users/created-at
          :users/updated-at))

(defn uuid []
  (java.util.UUID/randomUUID))
