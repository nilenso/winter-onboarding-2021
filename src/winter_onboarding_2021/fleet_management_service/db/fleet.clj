(ns winter-onboarding-2021.fleet-management-service.db.fleet
  (:require [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.error :as error]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.specs :as spec]))

(defn create [fleet]
  (if (s/valid? ::spec/fleets fleet)
    (db/insert! :fleets fleet)
    error/validation-failed))
