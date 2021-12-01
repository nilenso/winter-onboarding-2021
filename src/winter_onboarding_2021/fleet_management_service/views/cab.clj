(ns winter-onboarding-2021.fleet-management-service.views.cab)

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
