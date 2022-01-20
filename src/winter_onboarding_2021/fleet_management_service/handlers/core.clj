(ns winter-onboarding-2021.fleet-management-service.handlers.core
  (:require [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.db.healthcheck :as healthcheck]
            [winter-onboarding-2021.fleet-management-service.views.core :as view]))

(defn index [_]
  (view/index))

(defn health-check [_]
  (response/response {:message "Everything OK!"
                      :data (healthcheck/healthcheck-query)}))

(defn root [_]
  {:title "Landing Page"
   :content (view/index)})

(defn not-found [_]
  {:title "Not Found"
   :content "Page you requested is not found."})
