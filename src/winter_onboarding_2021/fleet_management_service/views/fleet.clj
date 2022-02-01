(ns winter-onboarding-2021.fleet-management-service.views.fleet
  (:require [winter-onboarding-2021.fleet-management-service.utils :as utils]))

(defn create-fleet []
  [:div {:id "content"}
   [:h1 {:class "text-success"} "Create Fleet"]
   [:form {:action "/fleets" :method "POST"}
    [:div [:label {:for "name" :class "form-label"} "Fleet Name"]
     [:input {:class "form-control" :type "text" :name "name" :id "name" :value ""}]]
    [:button {:type "submit" :class "btn btn-primary"} "Submit"]]])

(def headers
  [{:label "Name" :value :fleets/name}
   {:label "Managers" :value :managers}
   {:label "Created At" :value :fleets/created-at}])

(defn managers-list [managers]
  (map (fn [manager] [:div (:users/name manager)])
       managers))

(defn fleet-row [{:fleets/keys [id name created-at managers]}]
  [:tr
   [:td [:a {:href (str "/fleets/" id)
             :class "link-primary"}
         name]]
   [:td [:div (managers-list managers)]]
   [:td (utils/format-date created-at)]])

(defn show-fleets [fleets-with-managers page-num show-prev-page? show-next-page?]
  (let [next-page-query (str "?page=" (inc page-num))
        prev-page-query (str "?page=" (dec page-num))]
    [:div
     [:table {:class "table table-primary min-vh-40"}
      [:thead [:tr (map (fn [header] [:th (:label header)])
                        headers)]]
      [:tbody (map fleet-row fleets-with-managers)]]
     [:div {:class "text-end"}
      (if show-prev-page?
        [:a {:id "fleet-prev-page" :href prev-page-query}  "< Prev Page "] ())
      (if show-next-page?
        [:a {:id "fleet-next-page" :href next-page-query}  "Next Page > "] ())]]))
