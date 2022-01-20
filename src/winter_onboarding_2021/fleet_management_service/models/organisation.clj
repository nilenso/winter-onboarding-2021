(ns winter-onboarding-2021.fleet-management-service.models.organisation
  (:require [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.error :as errors]
            [winter-onboarding-2021.fleet-management-service.db.organisation :as org-db]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-models]))

(defn create [org]
  (let [org-with-valid-keys (utils/select-keys-from-spec org ::specs/organisations)]
    (if (empty? org-with-valid-keys)
      errors/no-valid-keys
      (org-db/create org-with-valid-keys))))

(defn create-and-associate [org admin]
  (let [db-org (create (assoc org
                              :organisations/created-by (:users/id admin)))]
    (user-models/add-to-org db-org admin)))
