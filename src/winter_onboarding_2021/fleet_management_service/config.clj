(ns winter-onboarding-2021.fleet-management-service.config
  (:require [mount.core :as mount :refer [defstate]]
            [aero.core :refer (read-config)]
            [clojure.java.io :as io]))

(defn env []
  (get (System/getenv) "ENV"))

(defn profile []
  (or (keyword (env)) :dev))

(defstate config
  :start (read-config (io/resource "config.edn")
                      {:profile (profile)})
  :stop nil)

(defn get-page-size []
  (:default-page-size config))

(defn get-log-config []
  (:log config))

(defn get-timeout-period []
  (:session-timeout-ms config))

(defn get-host-url []
  (:host-url config))
