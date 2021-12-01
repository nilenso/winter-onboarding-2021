(ns winter-onboarding-2021.fleet-management-service.views.cab
  (:require [camel-snake-kebab.core :as csk]))

(defn labelled-text-input [label & args]
  (let [options (apply hash-map args)
        name (csk/->snake_case_string label)]
    [:div
     [:label {:for label :class "form-label"} label]
     [:input (merge {:class "form-control" :type "text" :name name}
                    options)]]))

(defn cab-form [{:keys [name distance_travelled licence_plate]}]
  [:div {:id "content"}
   [:h1 {:class "text-success"} "This is the form page"]
   [:form {:action "/cabs/" :method "POST" :enctype "multipart/form-data"}
    [:div {:class "mb-3"}
     (labelled-text-input
      "Cab Name"
      :name "name"
      :required true
      :value name)]
    [:div {:class "mb-3"}
     (labelled-text-input
      "Licence Plate"
      :required true
      :value licence_plate)]
    [:div {:class "mb-3"}
     (labelled-text-input
      "Distance Travelled"
      :type "number"
      :required true
      :value distance_travelled)]
    [:button {:type "submit" :class "btn btn-primary"} "Submit"]]])
