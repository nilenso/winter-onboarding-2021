(ns winter-onboarding-2021.fleet-management-service.routes
  (:require [bidi.ring :as br]
            [ring.util.response :as response]
            [ring.middleware.json :refer [wrap-json-response]]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as cab-handlers]
            [winter-onboarding-2021.fleet-management-service.handlers.fleet :as fleet-handlers]
            [winter-onboarding-2021.fleet-management-service.handlers.core :as handler]
            [winter-onboarding-2021.fleet-management-service.handlers.user :as user-handlers]
            [winter-onboarding-2021.fleet-management-service.handlers.dashboard :as dashboard-handlers]
            [winter-onboarding-2021.fleet-management-service.handlers.organisation :as org-handlers]
            [winter-onboarding-2021.fleet-management-service.models.cab :as cab-model]))

(defn authentication-required [handler allowed-roles]
  (fn [request]
    (if-let [user-role (keyword (get-in request [:user :users/role]))]
      (if (contains? allowed-roles user-role)
        (handler request)
        (user-handlers/not-authorized request))
      (user-handlers/not-logged-in request))))

#_(def routes
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
                       :post fleet-handlers/create-fleet}
                   "/new" {:get fleet-handlers/new}}]
        ["organisations" {"/new" {:post (authentication-required org-handlers/create
                                                                 #{:admin})}}]
        ["fleets" {"" {:get (authentication-required fleet-handlers/show-fleets #{:admin})
                       :post (authentication-required fleet-handlers/create-fleet #{:admin})}
                   "/new" {:get (authentication-required fleet-handlers/new #{:admin})}}]
        ["healthcheck" {:get (wrap-json-response handler/health-check)}]
        ["index" {:get handler/index}]
        ["" {:get handler/root}]
        [true handler/not-found]]])


(defn- has-role [{:keys [user]} allowed-roles]
  ((:users/role user) allowed-roles))


(def routes'
  ["/" [["public" {:get (br/->Resources {:prefix "/bootstrap"})}]
        ["" {:get (fn [req] (response/response "Home page"))}]
        [["cabs" :slug] {:get :view-cab-get
                         #_(fn [req] (response/response "View cab page"))}]]])



;; PURPOSE:
;; Check constraints like org-id & registered? 
(def route->handler
    #(let [user (:user %)
           slug (:slug %)
           {:users/keys [org-id role]} user]
       {:view-cab-get {:allowed-role? (has-role % #{:admin :manager :driver})
                       :privilaged? (let [org-id-of-cab (-> slug
                                                       cab-model/get-by-id-or-licence-plate
                                                       :cabs/org-id)]
                                 (case role
                                   :driver (= org-id org-id-of-cab)
                                   :manager (= org-id org-id-of-cab)
                                   :admin (= org-id org-id-of-cab)))
                       :handler cab-handlers/view-cab}
        :update-cab-get {}
        :update-cab-post {}
        :public-route {}}))

(defn custom-middleware [handler-keyword]
  (fn [request]
    (let [results (handler-keyword (route->handler request))]
      (if (and (:allowed-role? results)
               (:priveleged? results))
        ((:handler results) request)
        (response/response "Not authorized")))))


(-> components/cab-page
    components/delete-btn)

(-> components/cab-page
    (place-in-between components/delete-button))

(defn place-in-between [parent child]
  (let [children (nth parent 3)]
    (.)))

(defn delete-btn [position element]
  (list element
        [:button {type""}]))
