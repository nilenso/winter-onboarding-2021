(ns winter-onboarding-2021.fleet-management-service.views.layout
  (:require [hiccup.page :refer [include-css include-js]]))

(defn application [{:keys [user flash]} title content]
  [:head
   [:title title]
   [:link {:rel "icon" :href "/public/favicon.ico" :type "image/x-icon"}]
   (include-css "/public/css/bootstrap.min.css")
   (include-js "/public/js/jquery-3.6.0.min.js")
   (include-js "/public/js/bootstrap.bundle.min.js")
   [:header {:class "d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom"}
    [:a {:class "d-flex align-items-center mb-3 mb-md-0 me-md-auto text-dark text-decoration-none"
         :href "/"}
     [:svg.bi.me-2
      {:height "32", :width "40"}
      [:use {:xlink:href "#bootstrap"}]]
     [:span {:class "fs-4"} "Fleet Management System"]]
    (if user
      [:ul {:class "nav nav-pills"}
       [:li {:class "nav-item"} [:a {:class "nav-link" :href "#"} (:users/name user)] ]
       [:li {:class "nav-item"} [:a {:class "nav-link" :href "/cabs"} "Cabs"]]
       [:li {:class "nav-item"} [:a {:class "nav-link" :href "/users/logout"} "Logout"]]]
      [:ul {:class "nav nav-pills"}
       [:li {:class "nav-item"} [:a {:class "nav-link" :href "/users/signup"} "Sign up"]]
       [:li {:class "nav-item"} [:a {:class "nav-link" :href "/users/login"} "Login"]]])]
   [:body
    [:div {:class "container"}
     (when flash
       [:div {:class (:style-class flash)} (:message flash)])
     content]]])
