(ns winter-onboarding-2021.fleet-management-service.routes
  (:require [bidi.ring :as br]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.handler :as handler]))

(def routes
  ["/" [["public" (br/->Resources {:prefix "/bootstrap"})]
        ["cabs" {:post {"/" cab/add}}]
        ["healthcheck" handler/health-check]
        ["index" handler/index]
        [true (fn [_] {:status 404 :body "Not found"})]]])
