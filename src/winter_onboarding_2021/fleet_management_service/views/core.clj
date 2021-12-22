(ns winter-onboarding-2021.fleet-management-service.views.core
  (:require [camel-snake-kebab.core :as csk]))

(defn index []
  [:div {:id "content"}
   [:h1 {:class "text-success"} "Hello Hiccup & Bootstrap"]])

(defn labelled-text-input [label & args]
  (let [options (apply hash-map args)
        name (csk/->kebab-case-string label)]
    [:div
     [:label {:for label :class "form-label"} label]
     [:input (merge {:class "form-control" :type "text" :name name}
                    options)]]))
