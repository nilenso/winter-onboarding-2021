(ns winter-onboarding-2021.fleet-management-service.handlers.dashboard
  (:require [winter-onboarding-2021.fleet-management-service.views.organisation :as org-views]))

(defn index [request]
  (let [user (:user request)
        response {:title "Dashboard"}]
    (if (:users/org-id user)
      (assoc response
             :content [:div "Dashboard thingy"])
      (assoc response
             :content (org-views/create)))))
