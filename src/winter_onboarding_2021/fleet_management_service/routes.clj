(ns winter-onboarding-2021.fleet-management-service.routes
  (:require [bidi.ring :as br]
            [ring.middleware.json :refer [wrap-json-response]]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.handler :as handler]))

(def routes
  ["/" [["public" {:get (br/->Resources {:prefix "/bootstrap"})}]
        ["cabs/" {"" {:post cab/create}
                  "new" {:get cab/new}
                  ["" :slug] {:get cab/view-cab}}]
        ["healthcheck" {:get (wrap-json-response handler/health-check)}]
        ["index" {:get handler/index}]
        [true (fn [_] {:status 404 :body "Not found"})]]])
