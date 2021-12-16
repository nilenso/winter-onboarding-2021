(ns winter-onboarding-2021.fleet-management-service.models.user
  (:require [winter-onboarding-2021.fleet-management-service.db.user :as user]))

(defn create [user]
  (user/create user))

(defn find-by-keys [key-map]
  (user/find-by-keys key-map))
