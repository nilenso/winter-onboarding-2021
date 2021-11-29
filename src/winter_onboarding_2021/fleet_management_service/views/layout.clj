(ns winter-onboarding-2021.fleet-management-service.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)]
        [winter-onboarding-2021.fleet-management-service.views.content :as content]))

(defn application [request title & content]
  (html5 [:head
          [:title title]
          [:link {:rel "icon" :href "public/favicon.ico" :type "image/x-icon"}]
          (include-css "/public/css/bootstrap.min.css")
          (include-js "/public/js/jquery-3.6.0.min.js")
          (include-js "/public/js/bootstrap.bundle.min.js")
          [:body
           [:div {:class "container"}
            (when (:flash request)
              [:div {:class (get-in request [:flash :style-class])} (get-in request [:flash :message])])
            content]]]))
