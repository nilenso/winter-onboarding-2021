(ns winter-onboarding-2021.fleet-management-service.db.user
  (:require [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]))

(defn create [user]
  (if (s/valid? ::specs/users user)
    (db/insert! :users user)
    {:error :validation-failed}))

(defn find-by-keys [key-map]
  (db/find-by-keys! :users key-map))
