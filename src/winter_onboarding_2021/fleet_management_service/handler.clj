(ns winter-onboarding-2021.fleet-management-service.handler
  (:require [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.views.content :as content]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]))

(def dummy-cabs
  [{:id 1
    :name "Cab 1"}
   {:id 2
    :name "Cab 2"}
   {:id 3
    :name "Cab 3"}
   {:id 4
    :name "Cab 4"}])

(defn fetch-all-cabs [request]
  (response/response (layout/application "Fleet Management Service"
                                         (content/show-cabs dummy-cabs))))

(defn index [request]
  (response/response (layout/application "Fleet Management Service"
                                         (content/index))))

(defn health-check [request]
  (response/response {:message "Everything OK!"
                      :data (db/healthcheck-query)}))
