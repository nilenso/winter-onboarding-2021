(ns winter-onboarding-2021.fleet-management-service.views.content)

(defn index []
  [:div {:id "content"}
   [:h1 {:class "text-success"} "Hello Hiccup & Bootstrap"]])

(defn show-cabs [cabs]
  (let [head [:id :name]]
    [:table {:class "table table-dark"}
     [:thead
      [:tr
       (for [col head] [:th col])]]
     [:tbody
      (for [row cabs]
        [:tr
         (for [cell row] [:td cell])])]]))
