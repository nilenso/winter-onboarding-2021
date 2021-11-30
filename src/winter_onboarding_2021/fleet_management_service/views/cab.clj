(ns winter-onboarding-2021.fleet-management-service.views.cab)

(defn show-cabs [cabs page-num]
  (let [head [:Name :Licence-plate :Distance-travelled :created-at :updated-at]
        next-page-query (str "?page=" (+ page-num 1))]
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
      [:a {:id "cab-next-page" :href next-page-query}  "Next Page > "]]]))