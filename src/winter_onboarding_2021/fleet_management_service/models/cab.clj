(ns winter-onboarding-2021.fleet-management-service.models.cab
  (:require  [winter-onboarding-2021.fleet-management-service.db.core :as db]
             [winter-onboarding-2021.fleet-management-service.models.core :as m-core]))

(defn create [cab]
  (m-core/insert! db/db-conn :cabs cab))
