(ns winter-onboarding-2021.fleet-management-service.routes
  (:require [bidi.ring :as br]
            
            [ring.middleware.json :refer [wrap-json-response]]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as cab-handlers]
            [winter-onboarding-2021.fleet-management-service.handlers.fleet :as fleet-handlers]
            [winter-onboarding-2021.fleet-management-service.handlers.core :as handler]
            [winter-onboarding-2021.fleet-management-service.handlers.user :as user-handlers]
            [winter-onboarding-2021.fleet-management-service.handlers.invite :as invite-handlers]
            [winter-onboarding-2021.fleet-management-service.handlers.dashboard :as dashboard-handlers]
            [winter-onboarding-2021.fleet-management-service.handlers.organisation :as org-handlers]))

(defn authentication-required [handler allowed-roles]
  (fn [request]
    (if-let [user-role (keyword (get-in request [:user :users/role]))]
      (if (contains? allowed-roles user-role)
        (handler request)
        (user-handlers/not-authorized request))
      (user-handlers/not-logged-in request))))

(def routes
  ["/" [["public" {:get (br/->Resources {:prefix "/bootstrap"})}]
        ["cabs" {"" {:get (authentication-required cab-handlers/get-cabs #{:admin :manager})
                     :post cab-handlers/create}
                 "/new" {:get (authentication-required  cab-handlers/new #{:admin :manager})}
                 "/delete" {:post (authentication-required  cab-handlers/delete #{:admin :manager})}
                 ["/" :slug] {"/edit" {:get cab-handlers/update-cab-view}
                              :get cab-handlers/view-cab
                              :post cab-handlers/update-cab}}]
        ["users" {"/signup" {:get user-handlers/signup-form
                             :post user-handlers/create-user}
                  "/login" {:get user-handlers/login-form
                            :post user-handlers/login}
                  "/logout" {:get user-handlers/logout}
                  "/dashboard" {:get (authentication-required dashboard-handlers/index
                                                              #{:admin :manager :driver})}}]
        ["fleets" {"" {:get (authentication-required fleet-handlers/show-fleets #{:admin})
                       :post (authentication-required fleet-handlers/create-fleet #{:admin})}
                   "/new" {:get (authentication-required fleet-handlers/new #{:admin})}}]
        ["invites" {"" {:post (authentication-required invite-handlers/create #{:admin})}
                    "/new" {:get (authentication-required invite-handlers/invites-page #{:admin})}}]
        ["organisations" {"/new" {:post (authentication-required org-handlers/create
                                                                 #{:admin})}}]
        ["healthcheck" {:get (wrap-json-response handler/health-check)}]
        ["index" {:get handler/index}]
        ["" {:get handler/root}]
        [true handler/not-found]]])
