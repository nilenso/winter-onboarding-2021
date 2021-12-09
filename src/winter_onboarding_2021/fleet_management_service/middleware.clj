(ns winter-onboarding-2021.fleet-management-service.middleware
  (:require [clojure.walk :as walk]
            [taoensso.timbre :as timbre]
            [ring.logger :as logger]
            [ring.middleware.stacktrace :as stacktrace]
            [ring.util.response :as response]))

(defn keywordize-multipart-params [handler]
  (fn [{:keys [multipart-params] :as request}]
    (-> request
        (assoc :multipart-params
               (walk/keywordize-keys multipart-params))
        handler)))

(defn wrap-exception-handler [handler]
  (fn [request]
    (try (handler request)
         (catch Exception _
           (-> (response/response "Something bad happened")
               (response/status 500))))))

(defn ring-timbre-logger [handler]
  (-> handler
      stacktrace/wrap-stacktrace-log
      (logger/wrap-with-logger {:log-fn (fn [{:keys [level throwable message]}]
                                          (if (some? throwable)
                                            (timbre/log level throwable message)
                                            (timbre/log level message)))})))
