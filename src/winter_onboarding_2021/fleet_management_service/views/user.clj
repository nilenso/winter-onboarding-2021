(ns winter-onboarding-2021.fleet-management-service.views.user)


(defn signup-form []
  [:form {:action "/user/signup" :method "POST"}
   [:div {:class "mb-3"}
    [:label.form-label {:for "signup-email-input"} "Email address"]
    [:input#signup-email-input.form-control 
     {:type "email", :name "email"}]]
   [:div {:class "mb-3"}
    [:label.form-label {:for "signup-name-input"} "Name"]
    [:input#signup-name0inout.form-control
     {:name "name"}]]
   [:div {:class "mb-3"}
    [:label.form-label {:for "signup-pwd-input"} "Password"]
    [:input#signup-pwd-input.form-control {:type "password", :name "password"}]]
   [:button.btn.btn-primary {:type "submit"} "Submit"]])
