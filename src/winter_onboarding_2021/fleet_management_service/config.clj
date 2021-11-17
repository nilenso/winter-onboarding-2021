(ns winter-onboarding-2021.fleet-management-service.config
  (:require [mount.core :as mount :refer [defstate]]
            [aero.core :refer (read-config)]))

(defstate config
  :start (read-config "config.edn"))
