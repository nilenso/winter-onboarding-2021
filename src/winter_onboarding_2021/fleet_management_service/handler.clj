(ns winter-onboarding-2021.fleet-management-service.handler
  (:require [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.views.content :as content]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]))

(defn index [request]
  (response/response (layout/application "Fleet Management Service"
                                         (content/index))))

(defn health-check [request]
  (response/response {:message "Everything OK!"
                      :data (db/healthcheck-query)}))

(defn add-cab [request]
  (println "shivam" request)
  (response/response {:body "hello"}))

