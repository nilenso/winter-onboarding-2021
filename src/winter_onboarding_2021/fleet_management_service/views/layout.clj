(ns winter-onboarding-2021.fleet-management-service.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)]
        [winter-onboarding-2021.fleet-management-service.views.content :as content]))

(defn application [title & content]
  (html5 [:head
          [:title title]
          (include-css "/css/bootstrap.min.css")
          (include-js "/js/jquery-3.6.0.min.js")
          (include-js "/js/bootstrap.bundle.min.js")
          [:body
           [:div {:class "container"} content]]]))
