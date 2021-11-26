(ns winter-onboarding-2021.fleet-management-service.handlers.core
  (:require [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.views.core :as view]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]))

(defn index [request]
  (response/response (layout/application "Fleet Management Service"
                                         (view/index))))

(defn health-check [_]
  (response/response (str {:message "Everything OK!"
                           :data (db/healthcheck-query)})))

(comment "extract body from request")
(comment "run validations on body")
(comment "save it to DB ") #_(db/insert-into-db :cabs data)
(comment "return the new cab with 200 status")
