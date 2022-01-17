(ns winter-onboarding-2021.fleet-management-service.views.invite)

(def headers
  [{:label "Token" :value :invites/token}
   {:label "Role" :value :invites/role}
   {:label "Valid Until" :value :invites/valid-until}
   {:label "Usages Limit" :value :invites/usages-done}
   {:label "Status" :value :invites/status}])

(defn invite-form []
  [:div.col-md-6
   [:aside
    [:p "Generate a new token:"]
    [:div.card
     [:article.card-body
      [:form {:action "/invites" :method "POST"}
       [:div.form-group
        [:label "Select Role"]
        [:select
         {:name "role" :class "form-select"}
         [:option {:value "admin"} "Admin"]
         [:option {:value "manager"} "Manager"]
         [:option {:value "driver"} "Driver"]]]
       [:br]
       [:div.form-group
        [:label "Number of accounts:"]
        [:input#usages
         {:max "50", :min "1", :name "usage-limit", :type "number"}]]
       [:br]
       [:div.form-group
        [:label {:for "valid_until"} "Valid Until :"]
        [:input#valid_until {:name "valid-until", :type "date"}]]
       [:br]
       [:div.form-group
        [:button.btn.btn-primary.btn-block
         {:type "submit"} "Create Token"]]]]]]])


(defn format-date [date]
  (.format (java.text.SimpleDateFormat. "MM/dd/yyyy") date))

(defn invite-row [row]
  [:tr
   [:td [:a {:href (str "/tokens/" (:invites/id row))
             :class "link-primary"}
         (:invites/token row)]]
   [:td (:invites/role row)]
   [:td (format-date (:invites/valid-until row))]
   [:td (:invites/usage-limit row)]
   [:td (:invites/status row)]])

(defn invites-not-found []
  [:div.col-md-6 {:id "content" :class "p-5"}
   [:h2 "No invites found"]])

(defn show-invites [invites]
  (if (empty? invites)
    (invites-not-found)
    [:div.col-md-6
     [:table {:class "table table-primary min-vh-40"}
      [:thead [:tr (map (fn [header] [:th (:label header)])
                        headers)]]
      [:tbody (map invite-row invites)]]]))

(defn invites-page [invites]
  [:div.row
   (invite-form)
   (show-invites invites)])
