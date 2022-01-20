(ns winter-onboarding-2021.fleet-management-service.models.fleet
  (:require [winter-onboarding-2021.fleet-management-service.db.fleet :as fleet-db]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.error :as error]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.models.users-fleets :as users-fleets-models]))

(defn create [tx fleet]
  (let [fleet-with-valid-keys (utils/select-keys-from-spec fleet ::specs/fleets)]
    (if (empty? fleet-with-valid-keys)
      error/no-valid-keys
      (fleet-db/create tx fleet-with-valid-keys))))

(defn create-and-associate [tx fleet user]
  (let [fleet (create tx fleet)
        _ (users-fleets-models/create tx user fleet)]
    fleet))

(defn total []
  (:count (first (fleet-db/total))))

(defn- user-fleets
  ([tx user-id] (fleet-db/user-fleets tx user-id 0 (config/get-page-size)))
  ([tx user-id off lim] (fleet-db/user-fleets tx user-id off lim)))

(defn- managers [tx fleet]
  (fleet-db/managers tx fleet))

(defn- append-managers [tx fleet]
  (assoc fleet
         :fleets/managers
         (managers tx fleet)))

(defn fleets-with-managers [tx user-id off lim]
  (let [fleets (user-fleets tx user-id off lim)]
    (doall (map #(append-managers tx %) fleets))))
