(ns winter-onboarding-2021.fleet-management-service.views.cab)

(defn show-cabs [cabs]
  (let [head [:id :name :licence-plate :distance-travelled :created-at :updated-at]]
    [:table {:class "table table-dark"}
     [:thead
      [:tr
       (for [col head] [:th col])]]
     [:tbody
      (for [row cabs]
        [:tr
         (for [cell row] [:td cell])])]]))
