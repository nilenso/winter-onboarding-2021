(ns winter-onboarding-2021.fleet-management-service.core
  (:require [ring.adapter.jetty :as raj]
            [ring.logger :as logger]
            [bidi.ring :as br]
            [mount.core :as mount :refer [defstate]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [winter-onboarding-2021.fleet-management-service.routes :as r]
            [winter-onboarding-2021.fleet-management-service.middleware :as middleware])
  (:gen-class))

(def middleware
  (->
   r/routes
   br/make-handler
   middleware/keywordize-multipart-params
   wrap-multipart-params
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
  [& args]
  (mount/start))
