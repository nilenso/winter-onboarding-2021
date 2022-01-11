(ns winter-onboarding-2021.fleet-management-service.models.fleet
  (:require [winter-onboarding-2021.fleet-management-service.db.fleet :as fleet-db]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.error :as error]))

(defn create [fleet]
  (let [fleet-with-valid-keys (utils/select-keys-from-spec fleet ::specs/fleets)]
    (if (empty? fleet-with-valid-keys)
      error/no-valid-keys
      (fleet-db/create fleet-with-valid-keys))))
