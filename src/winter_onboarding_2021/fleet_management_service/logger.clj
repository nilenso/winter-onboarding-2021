(ns winter-onboarding-2021.fleet-management-service.logger
  (:require [mount.core :as mount :refer [defstate]]
            [taoensso.timbre :as timbre]
            [winter-onboarding-2021.fleet-management-service.config :as config]))

(defstate logger
  :start (timbre/merge-config! (config/get-log-config)))
