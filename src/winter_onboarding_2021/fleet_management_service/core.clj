(ns winter-onboarding-2021.fleet-management-service.core
  (:require [ring.adapter.jetty :as raj]
            [winter-onboarding-2021.fleet-management-service.routes :as routes]
            [aero.core :refer (read-config)]
            [ring.logger :as logger]
            [mount.core :as mount :refer [defstate]])
  (:gen-class))

(defn start-server []
  (raj/run-jetty
   (logger/wrap-with-logger routes/ring-handler)
   {:port 3000
    :join? false}))

(defn stop-server [server]
  (when server (.stop server)))

(defstate config
  :start (read-config "config.edn"))

(defstate server
  :start (start-server)
  :stop (stop-server server))

(defn restart-server []
  (mount/stop)
  (mount/start))

(defn -main
  []
  (println "this is from fleet")
  (mount/start))
