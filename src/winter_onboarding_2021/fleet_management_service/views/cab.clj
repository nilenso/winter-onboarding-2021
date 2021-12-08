(ns winter-onboarding-2021.fleet-management-service.views.cab
  (:require [camel-snake-kebab.core :as csk]))

(defn labelled-text-input [label & args]
  (let [options (apply hash-map args)
        name (csk/->kebab-case-string label)]
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

(defn cab [cab]
  [:div {:id "content" :class "p-5"}
   [:h2 (:cabs/name cab)]
   [:div {:class "mt-5"}
    [:div "Licence Plate"]
    [:h3 (:cabs/licence-plate cab)]]
   [:div {:class "mt-5"}
    [:div "Distance Travlled"]
    [:h3 (:cabs/distance-travelled cab)]]
   [:a {:id "cab-next-page" :href (str "/cabs/" (:cabs/id cab) "/edit")} "Update > "]])

(defn show-cabs [cabs page-num show-next-page?]
  (let [head [:name :licence-plate :distance-travelled :created-at :updated-at]
        next-page-query (str "?page=" (inc page-num))]
    [:div
     [:table {:class "table table-dark min-vh-40"}
      [:thead [:tr
               (map (fn [col] [:th (str col)])
                    head)]]
      [:tbody (map
               (fn [row] [:tr (map
                               (fn [cell] [:td (str cell)])
                               row)])
               cabs)]]
     [:div {:class "text-end"}
      (if show-next-page?
        [:a {:id "cab-next-page" :href next-page-query}  "Next Page > "] ())]]))

(defn update-cab-form [{:cabs/keys [id name distance-travelled]}]
  (let [route (str "/cabs/" id)]
    [:div {:id "content"}
     [:h1 {:class "text-success"} "Update Cab"]
     [:form {:action route :method "POST" :enctype "multipart/form-data"}
      [:div
       [:div {:class "mb-3"}
        (labelled-text-input
         "Cab Name"
         :name "name"
         :required true
         :value name)]
       [:div {:class "mb-3"}
        (labelled-text-input
         "Distance Travelled"
         :type "number"
         :required true
         :value distance-travelled)]
       [:button {:type "submit" :class "btn btn-primary"} "Update"]]]]))
