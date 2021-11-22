(ns winter-onboarding-2021.fleet-management-service.core
  (:require [ring.adapter.jetty :as raj]
            [winter-onboarding-2021.fleet-management-service.routes :as routes]
            [ring.logger :as logger]
            [mount.core :as mount :refer [defstate]]
            [ring.middleware.resource :refer [wrap-resource]])
  (:gen-class))

(def middleware
  (-> (wrap-resource routes/ring-handler "bootstrap")
      logger/wrap-with-logger))

(defn start-server []
  (raj/run-jetty
   middleware
   {:port 3000
    :join? false}))

(defn stop-server [server]
  (when server (.stop server)))

(defstate server
  :start (start-server)
  :stop (stop-server server))

(defn -main
  []
  (println "this is from fleet")
  (mount/start))
