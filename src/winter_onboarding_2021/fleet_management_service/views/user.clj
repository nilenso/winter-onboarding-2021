(ns winter-onboarding-2021.fleet-management-service.views.user)


(defn signup-form []
  [:form {:action "/users/signup" :method "POST"}
   [:div {:class "mb-3" :id "email"}
    [:label {:for "signup-email-input" :class "form-label"} "Email address"]
    [:input {:required "required" :id "signup-email-input" :class "form-control" :type "email", :name "email"}]]
   [:div {:class "mb-3" :id "name"}
    [:label {:for "signup-name-input" :class "form-label"} "Name"]
    [:input {:required "required" :id "signup-name-input" :class "form-control" :name "name"}]]
   [:div {:class "mb-3" :id "password"}
    [:label {:for "signup-pwd-input" :class "form-label"} "Password"]
    [:input  {:required "required" :id "signup-pwd-input" :class "form-control" :type "password", :name "password"}]]
   [:button {:type "submit" :class "btn btn-primary"} "Submit"]])
