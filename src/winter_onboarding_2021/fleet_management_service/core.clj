(ns winter-onboarding-2021.fleet-management-service.core
  (:require [ring.adapter.jetty :as raj]
            [ring.logger :as logger]
            [bidi.ring :as br]
            [mount.core :as mount :refer [defstate]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.flash :refer [wrap-flash]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.session :refer [wrap-session]]
            [winter-onboarding-2021.fleet-management-service.logger]
            [winter-onboarding-2021.fleet-management-service.routes :as r]
            [winter-onboarding-2021.fleet-management-service.middleware :as middleware]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.migration :as migration])
  (:gen-class))

(def middleware
  (->
   r/routes
   br/make-handler
   wrap-flash
   middleware/keywordize-multipart-params
   wrap-multipart-params
   wrap-keyword-params
   wrap-params
   wrap-session
   logger/wrap-with-logger
   middleware/exception-logging))

(defn start-server []
  (let [port (if (int? (:port config/config))
               (:port config/config)
               (Integer/parseInt (:port config/config)))]
    (println (str "Starting server on port:") port)
    (raj/run-jetty middleware
                   {:port port
                    :join? false})))

(defn stop-server [server]
  (when server (.stop server)))

#_:clj-kondo/ignore
(defstate server
  :start (start-server)
  :stop (stop-server server))

(defn -main [command & rest]
  (if (= "migrations" command)
    (apply migration/run-migratus rest)
    (mount/start)))
