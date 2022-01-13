(ns winter-onboarding-2021.fleet-management-service.views.organisation
  (:require [winter-onboarding-2021.fleet-management-service.views.core :as core-views]))

(defn create []
  [:div {:class "d-flex justify-content-center align-items-center"}
   [:div {:class "w-25 h-50 p-5 mb-4 bg-light rounded-4 text-center"}
    [:h1 "Organisation"]
    [:div
     [:form {:action "/organisations/new" :method "POST"}
      [:div {:class "mb-3"}
       (core-views/labelled-text-input
        ""
        :id "name"
        :name "name"
        :required true
        :placeholder "Enter organisation name")]
      [:button {:type "submit" :class "btn btn-primary mt-2"} "Create"]]]]])
