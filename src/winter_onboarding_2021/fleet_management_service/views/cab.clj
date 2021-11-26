(ns winter-onboarding-2021.fleet-management-service.views.cab)

(defn add-cab-form [data]
  [:div {:id "content"}
   [:h1 {:class "text-success"} "This is the form page"]
   [:form {:action "/cabs/" :method "POST" :enctype "multipart/form-data"}
    [:div {:class "mb-3"}
     [:label {:for "Cab Name" :class "form-label"} "Cab Name"]
     [:input {:type "text"
              :name "name"
              :class "form-control"
              :id "cabName"
              :required true
              :value (:name data)}]]
    [:div {:class "mb-3"}
     [:label {:for "Licence Plate" :class "form-label"} "Licence Plate"]
     [:input {:type "text"
              :name "licence_plate"
              :class "form-control"
              :id "licencePlate"
              :required true
              :value (:licence_plate data)}]]
    [:div {:class "mb-3"}
     [:label {:for "Distance Travelled" :class "form-label"} "Distance Travelled"]
     [:input {:type "text"
              :name "distance_travelled"
              :class "form-control"
              :id "distanceTravelled"
              :required true
              :value (:distance_travelled data)}]]
    [:button {:type "submit" :class "btn btn-primary"} "Submit"]]])
