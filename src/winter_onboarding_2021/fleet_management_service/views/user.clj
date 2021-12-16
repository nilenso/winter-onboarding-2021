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
   [:input {:type "hidden" :name "role" :value "admin"}]
   [:button {:type "submit" :class "btn btn-primary"} "Submit"]])

(defn login-form []
  [:div [:h1 {:class "text-center"} "Login"] 
   [:form {:method "POST"}
   [:div {:class "mb-3" :id "email"}
    [:label {:for "login-email-input" :class "form-label"} "Email address"]
    [:input {:required :required :id "login-email-input" :class "form-control" :type "email", :name "email"}]]
   [:div {:class "mb-3" :id "password"}
    [:label {:for "login-pwd-input" :class "form-label"} "Password"]
    [:input  {:required :required :id "login-pwd-input" :class "form-control" :type "password", :name "password"}]]
   [:button {:type "submit" :class "btn btn-primary"} "Submit"]]])
