(ns winter-onboarding-2021.fleet-management-service.db.user
  (:require [honey.sql :as sql]
            [honey.sql.helpers :as h :refer [select from limit offset order-by update set where]]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]))


(defn create [user]
  (db/insert! :users user))
