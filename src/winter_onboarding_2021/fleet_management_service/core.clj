(ns winter-onboarding-2021.fleet-management-service.core
  (:require [ring.adapter.jetty :as raj]
            [ring.logger :as logger]
            [bidi.ring :as br]
            [mount.core :as mount :refer [defstate]]
            [ring.middleware.json :refer [wrap-json-response]]
            [winter-onboarding-2021.fleet-management-service.routes :as r]
            [winter-onboarding-2021.fleet-management-service.config :refer [config]])
  (:gen-class))

(def middleware
  (->
   (br/make-handler r/routes)
   wrap-json-response
   logger/wrap-with-logger))

(defn start-server []
  (println (str "Starting server on port:") (:port config))
  (raj/run-jetty middleware
                 {:port (Integer/parseInt (:port config))
                  :join? false}))

(defn stop-server [server]
  (when server (.stop server)))

(defstate server
  :start (start-server)
  :stop (stop-server server))

(defn -main
  [& args]
  (mount/start))
