(ns winter-onboarding-2021.fleet-management-service.config
  (:require [mount.core :as mount :refer [defstate]]
            [aero.core :refer (read-config)]
            [clojure.java.io :as io]))

(defn profile []
  (or (keyword (get (System/getenv) "ENV"))
      :dev))

(defstate config
  :start (read-config (io/resource "config.edn") {:profile (profile)})
  :stop nil)