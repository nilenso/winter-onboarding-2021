(ns winter-onboarding-2021.fleet-management-service.views.cab)

(defn labelled-text-input [label-class label inp-class inp-name inp-type required value]
  [:div
   [:label {:for label :class label-class} label]
   [:input {:type inp-type
            :name inp-name
            :class inp-class
            :required required
            :value value}]])

(defn cab-form [data]
  [:div {:id "content"}
   [:h1 {:class "text-success"} "This is the form page"]
   [:form {:action "/cabs/" :method "POST" :enctype "multipart/form-data"}
    [:div {:class "mb-3"}
     (labelled-text-input
      "form-label"
      "Cab Name"
      "form-control"
      "name"
      "text"
      true (:name data))]
    [:div {:class "mb-3"}
     (labelled-text-input
      "form-label"
      "Licence Plate"
      "form-control"
      "licence_plate"
      "text"
      true
      (:licence_plate data))]
    [:div {:class "mb-3"}
     (labelled-text-input
      "form-label"
      "Distance Travelled"
      "form-control"
      "distance_travelled"
      "text"
      true
      (:distance_travelled data))]
    [:button {:type "submit" :class "btn btn-primary"} "Submit"]]])
