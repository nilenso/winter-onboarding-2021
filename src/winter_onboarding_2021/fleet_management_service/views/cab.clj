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
    [:h3 (:cabs/distance-travelled cab)]]])

(def headers
  [{:label "Name" :value :cabs/name}
   {:label "Distance travelled" :value :cabs/distance-travelled}
   {:label "Licence plate" :value :cabs/licence-plate}
   {:label "Created at" :value :cabs/created-at}
   {:label "Updated at" :value :cabs/updated-at}])

(defn gen-cab-row [row]
  [:tr (map (fn [header] [:td (header row)])
            (map :value headers))])

(defn show-cabs [cabs page-num show-next-page?]
  (let [next-page-query (str "?page=" (inc page-num))]
    [:div
     [:table {:class "table table-dark min-vh-40"}
      [:thead [:tr (map (fn [header] [:th (:label header)])
                        headers)]]
      [:tbody (map gen-cab-row cabs)]]
     [:div {:class "text-end"}
      (if show-next-page?
        [:a {:id "cab-next-page" :href next-page-query}  "Next Page > "] ())]]))
