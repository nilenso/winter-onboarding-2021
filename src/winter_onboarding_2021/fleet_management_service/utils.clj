(ns winter-onboarding-2021.fleet-management-service.utils)

(defn string->uuid [id]
  (try (java.util.UUID/fromString id)
       (catch Exception _ nil)))
