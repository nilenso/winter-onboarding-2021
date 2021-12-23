(ns winter-onboarding-2021.fleet-management-service.views.fleet)

(defn create-fleet []
  [:div {:id "content"}
   [:h1 {:class "text-success"} "Create Fleet"]
   [:form {:action "/fleets" :method "POST"}
    [:div [:label {:for "name" :class "form-label"} "Fleet Name"]
     [:input {:class "form-control" :type "text" :name "name" :id "name" :value ""}]]
    [:button {:type "submit" :class "btn btn-primary"} "Submit"]]])
