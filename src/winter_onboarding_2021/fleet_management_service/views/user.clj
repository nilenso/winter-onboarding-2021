(ns winter-onboarding-2021.fleet-management-service.views.user
  (:require [winter-onboarding-2021.fleet-management-service.views.core :as core-views]))


(defn signup-form []
  [:form {:action "/users/signup" :method "POST"}
   [:div {:class "mb-3" :id "email"}
    (core-views/labelled-text-input
     "Email address"
     :required true
     :id "signup-email-input"
     :name "email"
     :type "email")]
   [:div {:class "mb-3" :id "name"}
    (core-views/labelled-text-input
     "Name"
     :required true
     :id "signup-name-input"
     :name "name")]
   [:div {:class "mb-3" :id "password"}
    (core-views/labelled-text-input
     "Password"
     :required true
     :id "signup-pwd-input"
     :type "password"
     :name "password")]
   [:button {:type "submit" :class "btn btn-primary"} "Submit"]])
