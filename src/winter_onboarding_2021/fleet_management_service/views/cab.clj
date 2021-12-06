(ns winter-onboarding-2021.fleet-management-service.views.cab
  (:require [camel-snake-kebab.core :as csk]))

(defn labelled-text-input [label & args]
  (let [options (apply hash-map args)
        name (csk/->snake_case_string label)]
    [:div
     [:label {:for label :class "form-label"} label]
     [:input (merge {:class "form-control" :type "text" :name name}
                    options)]]))

(defn cab-form [{:keys [name distance-travelled licence-plate]}]
  [:div {:id "content"}
   [:h1 {:class "text-success"} "This is the form page"]
   [:form {:action "/cabs" :method "POST" :enctype "multipart/form-data"}
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
      :value licence-plate)]
    [:div {:class "mb-3"}
     (labelled-text-input
      "Distance Travelled"
      :type "number"
      :required true
      :value distance-travelled)]
    [:button {:type "submit" :class "btn btn-primary"} "Submit"]]])

(defn show-cabs [cabs page-num show-next-page?]
  (let [head [:name :licence-plate :distance-travelled :created-at :updated-at]
        next-page-query (str "?page=" (inc page-num))]
    [:div
     [:table {:class "table table-dark min-vh-40"}
      [:thead
       [:tr
        (for [col head] [:th col])]]
      [:tbody
       (for [row cabs]
         [:tr
          (for [cell row] [:td cell])])]]
     [:div {:class "text-end"}
      (if show-next-page?
        [:a {:id "cab-next-page" :href next-page-query}  "Next Page > "] ())]]))
