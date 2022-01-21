(ns winter-onboarding-2021.fleet-management-service.db.organisation
  (:require [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.error :as errors]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]))

(defn create [tx organisation]
  (if (s/valid? ::specs/organisations organisation)
    (db/insert! tx :organisations organisation)
    (let [error-msg (s/explain-str ::specs/organisations organisation)]
      (assoc errors/validation-failed :error-msg error-msg))))
