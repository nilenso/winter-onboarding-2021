(ns winter-onboarding-2021.fleet-management-service.routes
  (:require [bidi.ring :as br]
            [ring.middleware.json :refer [wrap-json-response]]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.handler :as handler]))

(def routes
  ["/" [["public" (br/->Resources {:prefix "/bootstrap"})]
        ["cabs" {:post {"/" cab/add}
                 "/new" cab/serve-add-cab-form}]
        ["healthcheck" (wrap-json-response handler/health-check)]
        ["index" handler/index]
        [true (fn [_] {:status 404 :body "Not found"})]]])
