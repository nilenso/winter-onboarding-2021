(ns winter-onboarding-2021.fleet-management-service.views.fleet
  (:require [clojure.string :as string]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))

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
  (string/join ", " (map #(:users/name %) managers)))

(defn fleet-row [row]
  [:tr
   [:td [:a {:href (str "/fleets/" (:fleets/id row))
             :class "link-primary"}
         (:fleets/name row)]]
   [:td (managers-list (:managers row))]
   [:td (utils/format-date (:fleets/created-at row))]])

(defn show-fleets [fleets-with-managers page-num show-next-page?]
  (let [next-page-query (str "?page=" (inc page-num))]
    [:div
     [:table {:class "table table-primary min-vh-40"}
      [:thead [:tr (map (fn [header] [:th (:label header)])
                        headers)]]
      [:tbody (map fleet-row fleets-with-managers)]]
     [:div {:class "text-end"}
      (if show-next-page?
        [:a {:id "cab-next-page" :href next-page-query}  "Next Page > "] ())]]))
