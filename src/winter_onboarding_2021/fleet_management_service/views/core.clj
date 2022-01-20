(ns winter-onboarding-2021.fleet-management-service.views.core
  (:require [camel-snake-kebab.core :as csk]))

(defn index []
  [:div {:class "mt-5"}
   [:div {:class "h-100 p-5 bg-light border rounded-3 text-center"}
    [:h1 {:class "display-4"} "Welcome to fms"]
    [:p {:class "lead"} "fms is the simple alternative to spreadsheets and outdated fleet software - designed to help you automate fleet operations tasks and keep cabs running smoothly."]
    [:hr {:class "my-4"}]
    [:p "Currently fms is development phase. Beta release is scheduled on February."]
    [:br]
    [:p "Early bird demo booking available!"]
    [:a {:class "btn btn-primary btn-lg", :href "#", :role "button"} "Book Now &gt;"]]])

(defn labelled-text-input [label & args]
  (let [options (apply hash-map args)
        name (csk/->kebab-case-string label)]
    [:div
     [:label {:for label :class "form-label"} label]
     [:input (merge {:class "form-control" :type "text" :name name}
                    options)]]))
