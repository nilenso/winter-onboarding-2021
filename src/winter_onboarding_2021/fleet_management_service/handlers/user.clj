(ns winter-onboarding-2021.fleet-management-service.handlers.user
  (:require [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.views.user :as view]))

(defn signup-form [request]
  {:title "Sign-up"
   :content (view/signup-form)})
