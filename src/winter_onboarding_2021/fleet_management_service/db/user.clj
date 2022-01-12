(ns winter-onboarding-2021.fleet-management-service.db.user
  (:require [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.error :as error]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]))

(defn create [user]
  (if (s/valid? ::specs/users user)
    (db/insert! :users user)
    (let [error-msg (s/explain-str ::specs/users user)]
      (assoc error/validation-failed :error-msg error-msg))))

(defn find-by-keys [key-map]
  (db/find-by-keys! :users key-map))
