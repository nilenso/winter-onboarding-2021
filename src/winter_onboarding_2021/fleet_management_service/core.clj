(ns winter-onboarding-2021.fleet-management-service.core
  (:require [ring.adapter.jetty :as raj]
            [ring.logger :as logger]
            [bidi.ring :as br]
            [mount.core :as mount :refer [defstate]]
            [ring.middleware.json :refer [wrap-json-response]]
            [winter-onboarding-2021.fleet-management-service.routes :as r]
            [winter-onboarding-2021.fleet-management-service.migration :as migration]
            [winter-onboarding-2021.fleet-management-service.config :refer [config]])
  (:gen-class))

(def middleware
  (->
   (br/make-handler r/routes)
   wrap-json-response
   logger/wrap-with-logger))

(defn start-server []
  (let [port (if (int? (:port config))
               (:port config)
               (Integer/parseInt (:port config)))]
    (println (str "Starting server on port:") port)
    (raj/run-jetty middleware
                   {:port port
                    :join? false})))

(defn stop-server [server]
  (when server (.stop server)))

(defstate server
  :start (start-server)
  :stop (stop-server server))

(defn -main [command & rest]
  (if (= "migrations" command)
    (apply migration/run-migratus rest)
    (mount/start)))
