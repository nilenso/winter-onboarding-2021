(ns winter-onboarding-2021.fleet-management-service.routes
  (:require [bidi.ring :as br]
            [winter-onboarding-2021.fleet-management-service.handler :as handler]))

(def routes ["/" {"public" (br/->Resources {:prefix "/bootstrap"})
                  "cabs" handler/fetch-all-cabs
                  "healthcheck" handler/health-check
                  "index" handler/index}])
