(ns winter-onboarding-2021.fleet-management-service.core
  (:gen-class))

(println "Hello world")

(defn hello-world [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello from my API!"})

(defn -main
  []
  (println "this is from fleet"))
