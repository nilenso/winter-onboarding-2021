(ns winter-onboarding-2021.fleet-management-service.config
  (:require [mount.core :as mount :refer [defstate]]
            [aero.core :refer (read-config)]))

(defn profile []
  (or (keyword (get (System/getenv) "ENV"))
      :dev))

(defstate config
  :start (read-config "config/config.edn" {:profile (profile)})
  :stop nil)
