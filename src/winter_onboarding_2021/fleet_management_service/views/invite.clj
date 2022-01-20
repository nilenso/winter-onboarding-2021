(ns winter-onboarding-2021.fleet-management-service.views.invite
  (:require [winter-onboarding-2021.fleet-management-service.views.core :as core]))

(def ^:private headers
  [{:label "Token" :value :invites/token}
   {:label "Role" :value :invites/role}
   {:label "Valid Until" :value :invites/valid-until}
   {:label "Usages Limit" :value :invites/usages-done}
   {:label "Status" :value :invites/is-active}])

(defn invite-form []
  [:div.col-md-6
   [:aside
    [:p "Generate a new token:"]
    [:div {:class "card"}
     [:article {:class "card-body"}
      [:form {:action "/invites" :method "POST"}
       [:div {:class "form-group"}
        [:label {:class "form-label"} "Select Role:"]
        [:select
         {:name "role" :class "form-select" :required true}
         [:option {:value "admin"} "Admin"]
         [:option {:value "manager"} "Manager"]
         [:option {:value "driver"} "Driver"]]]
       (core/labelled-text-input "Number of accounts:"
                                 :required true
                                 :name "usage-limit"
                                 :type "number"
                                 :max 50
                                 :min 1)
       (core/labelled-text-input "Valid Until:"
                                 :required true
                                 :name "valid-until"
                                 :type "date")
       [:div
        [:button {:class "btn btn-primary btn-block"
                  :type "submit"}
         "Create Token"]]]]]]])

(defn format-date [date]
  (.format (java.text.SimpleDateFormat. "MM/dd/yyyy") date))

(defn invite-row [{:invites/keys [id token role valid-until usage-limit is-active]}]
  [:tr
   [:td [:a {:href (str "/tokens/" id)
             :class "link-primary"}
         token]]
   [:td role]
   [:td (format-date valid-until)]
   [:td usage-limit]
   [:td (if (true? is-active)
          "Active"
          "Disabled")]])

(defn invites-not-found []
  [:div {:id "content" :class "p-5 col-md-6"}
   [:h2 "No invites found"]])

(defn show-invites [invites]
  (if (empty? invites)
    (invites-not-found)
    [:div {:class "col-md-6"}
     [:table {:class "table table-primary min-vh-40"}
      [:thead [:tr (map (fn [header] [:th (:label header)])
                        headers)]]
      [:tbody (map invite-row invites)]]]))

(defn invites-page [invites]
  [:div {:class "row"}
   (invite-form)
   (show-invites invites)])
