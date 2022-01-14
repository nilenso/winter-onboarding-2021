(ns winter-onboarding-2021.fleet-management-service.routes
  (:require [bidi.ring :as br]
            [ring.util.response :as response]
            [ring.middleware.json :refer [wrap-json-response]]
            [hiccup.page :refer [html5]]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as cab-handlers]
            [winter-onboarding-2021.fleet-management-service.handlers.fleet :as fleet-handlers]
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

(defn authentication-required [handler allowed-roles]
  (fn [request]
    (if-let [user-role (keyword (get-in request [:user :users/role]))]
      (if (contains? allowed-roles user-role)
        (handler request)
        (user-handlers/not-authorized request))
      (user-handlers/not-logged-in request))))

(def routes
  ["/" [["public" {:get (br/->Resources {:prefix "/bootstrap"})}]
        ["cabs" {"" {:get (authentication-required (wrap-layout cab-handlers/get-cabs) #{:admin :manager})
                     :post cab-handlers/create}
                 "/new" {:get (authentication-required  (wrap-layout cab-handlers/new) #{:admin :manager})}
                 "/delete" {:post (authentication-required  (wrap-layout cab-handlers/delete) #{:admin :manager})}
                 ["/" :slug] {"/edit" {:get (wrap-layout cab-handlers/update-cab-view)}
                              :get (wrap-layout cab-handlers/view-cab)
                              :post cab-handlers/update-cab}}]
        ["users" {"/signup" {:get user-handlers/signup-form
                             :post user-handlers/create-user}
                  "/login" {:get user-handlers/login-form
                            :post user-handlers/login}
                  "/logout" {:get user-handlers/logout}}]
        ["fleets" {"" {:get (authentication-required (wrap-layout fleet-handlers/show-fleets) #{:admin})
                       :post (authentication-required fleet-handlers/create-fleet #{:admin})}
                   "/new" {:get (authentication-required (wrap-layout fleet-handlers/new) #{:admin})}}]
        ["healthcheck" {:get (wrap-json-response handler/health-check)}]
        ["index" {:get (wrap-layout handler/index)}]
        ["" {:get (wrap-layout handler/root)}]
        [true (wrap-layout handler/not-found)]]])
