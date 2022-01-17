(ns winter-onboarding-2021.fleet-management-service.db.invite
  (:require [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.error :as errors]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]))

(defn create [invite]
  (if (s/valid? ::specs/invites-create-model invite)
    (db/insert! :invites invite)
    errors/validation-failed))

(defn find-by-keys [key-map]
  (db/find-by-keys! :invites key-map))
