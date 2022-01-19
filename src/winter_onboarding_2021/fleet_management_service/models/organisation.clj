(ns winter-onboarding-2021.fleet-management-service.models.organisation
  (:require [winter-onboarding-2021.fleet-management-service.db.organisation :as org-db]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-models]))

(defn create [org]
  (org-db/create org))

(defn create-and-associate [org admin]
  (let [db-org (create (assoc org
                              :created-by (:users/id admin)))]
    (user-models/add-to-org db-org admin)))
