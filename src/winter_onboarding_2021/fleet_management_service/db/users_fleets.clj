(ns winter-onboarding-2021.fleet-management-service.db.users-fleets
  (:require [winter-onboarding-2021.fleet-management-service.db.core :as core-db]))

(defn create [user fleet]
  (core-db/insert! :users-fleets {:user-id (:users/id user)
                                  :fleet-id (:fleets/id fleet)}))
