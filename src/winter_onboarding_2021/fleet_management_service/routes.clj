(ns winter-onboarding-2021.fleet-management-service.routes
  (:require [bidi.ring :as br]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as cab]
            [ring.middleware.json :refer [wrap-json-response]]
            [winter-onboarding-2021.fleet-management-service.handler :as handler]))

(def routes
  ["/" [["public" (br/->Resources {:prefix "/bootstrap"})]
        ["cabs" {:get {"/" (wrap-json-response cab/get-cabs)}
                 :post {"/" cab/add}
                 "/new" (fn [_] {:status 200 :body "New cab form page"})}]
        ["healthcheck" handler/health-check]
        ["index" handler/index]
        [true (fn [_] {:status 404 :body "Not found"})]]])
