(ns winter-onboarding-2021.fleet-management-service.routes
  (:require [bidi.ring :as br]
            [ring.util.response :as response]
            [ring.middleware.json :refer [wrap-json-response]]
            [hiccup.page :refer [html5]]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as cab-handlers]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]
            [winter-onboarding-2021.fleet-management-service.handlers.core :as handler]
            [winter-onboarding-2021.fleet-management-service.handlers.user :as user-handlers]))

(defn wrap-layout [handler]
  (fn [request]
    (let [data (handler request)]
      (response/response (html5 (layout/application
                                 request
                                 (:title data)
                                 (:content data)))))))

(def routes
  ["/" [["public" {:get (br/->Resources {:prefix "/bootstrap"})}]
        ["cabs" {"" {:get (wrap-layout cab-handlers/get-cabs)
                     :post cab-handlers/create}
                 "/new" {:get (wrap-layout cab-handlers/new)}
                 "/delete" {:post cab-handlers/delete}
                 ["/" :slug] {"/edit" {:get (wrap-layout cab-handlers/update-cab-view)}
                              :get (wrap-layout cab-handlers/view-cab)
                              :post cab-handlers/update-cab}}]
        ["users" {"/signup" {:get (wrap-layout user-handlers/signup-form)
                             :post user-handlers/create-user}}]
        ["healthcheck" {:get (wrap-json-response handler/health-check)}]
        ["index" {:get (wrap-layout handler/index)}]
        ["" {:get (wrap-layout handler/root)}]
        [true (wrap-layout handler/not-found)]]])
