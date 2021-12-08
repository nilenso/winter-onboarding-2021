(ns winter-onboarding-2021.fleet-management-service.logger
  (:require [mount.core :as mount :refer [defstate]]
            [taoensso.timbre :refer [merge-config!]]
            [winter-onboarding-2021.fleet-management-service.config :as config]))

(defstate logger
  :start (merge-config! (config/get-log-config)))
