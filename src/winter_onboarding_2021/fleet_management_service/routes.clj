(ns winter-onboarding-2021.fleet-management-service.routes
  (:require [bidi.ring :as br]
            [ring.middleware.json :refer [wrap-json-response]]
            [winter-onboarding-2021.fleet-management-service.handler :as handler]))

(def routes ["/" {"cabs" handler/fetch-all-cabs
                  "healthcheck" handler/health-check
                  "healthcheck-db" handler/health-check-db}])

(def ring-handler (-> (br/make-handler routes)
                      wrap-json-response))
