(ns winter-onboarding-2021.fleet-management-service.views.fleet
  (:require [clojure.string :as string]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))

(defn- no-team-members-present [role]
  [:div (format "You have no %s in your organisation" role)])

(defn- checkbox-group [label members]
  [:fieldset {:class "mt-4 mb-4" :id label}
   [:legend (str "Add " (string/capitalize label))]
   (if (> (count members) 0)
     (map #(list [:input {:class "me-2" :type "checkbox" :name label :value (:users/id %)}]
                 [:label {:class "me-4"} (:users/name %)]) members)
     (no-team-members-present label))])

(defn create-fleet [org-team-members]
  (let [managers (filter #(= "manager" (:users/role %)) org-team-members)
        drivers (filter #(= "driver" (:users/role %)) org-team-members)]
    [:div {:id "content"}
     [:h1 {:class "text-success"} "Create Fleet"]
     [:form {:action "/fleets" :method "POST"}
      [:div [:label {:for "name" :class "form-label"} "Fleet Name"]
       [:input {:class "form-control" :type "text" :name "name" :id "name" :value ""}]]
      (checkbox-group "managers" managers)
      (checkbox-group "drivers" drivers)
      [:button {:type "submit" :class "btn btn-primary"} "Submit"]]]))

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
