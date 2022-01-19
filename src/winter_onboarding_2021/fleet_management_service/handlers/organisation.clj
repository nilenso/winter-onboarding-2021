(ns winter-onboarding-2021.fleet-management-service.handlers.organisation
  (:require [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.models.organisation :as org-models]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))

(defn create [{:keys [user form-params]}]
  (let [org-name (:name form-params)
        {:users/keys [org-id role]} user]
    (cond
      org-id (merge (utils/flash-msg "You are already associated with an organsation", false)
                    (response/redirect "/users/dashboard"))
      (not= "admin" role) (merge (utils/flash-msg "You need admin privileges to create an org", false)
                                 (response/redirect "/users/dashboard"))
      :else (do (org-models/create-and-associate {:organisations/name org-name}
                                                 user)
                (merge (utils/flash-msg "Organsiation created successfully", true)
                       (response/redirect "/users/dashboard"))))))
