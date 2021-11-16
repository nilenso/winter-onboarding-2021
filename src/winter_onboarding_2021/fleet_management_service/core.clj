(ns winter-onboarding-2021.fleet-management-service.core
  (:require [ring.adapter.jetty :as raj]
            [winter-onboarding-2021.fleet-management-service.routes :as routes])
  (:gen-class))

(defonce server (atom nil))

(defn hello-world [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello from my API!"})

(defn start-server []
  (reset! server (raj/run-jetty routes/ring-handler
                                {:port 3000
                                 :join? false})))

(defn stop-server []
  (when @server (.stop @server))
  (reset! server nil))

(defn restart-server []
  (stop-server)
  (start-server))

(defn -main
  []
  (println "this is from fleet")
  (start-server))
