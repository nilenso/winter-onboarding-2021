(ns winter-onboarding-2021.fleet-management-service.middleware
  (:require [clojure.walk :as walk]
            [taoensso.timbre :refer [error info]]
            [ring.util.response :as response]))

(defn keywordize-multipart-params [handler]
  (fn [{:keys [multipart-params] :as request}]
    (-> request
        (assoc :multipart-params
               (walk/keywordize-keys multipart-params))
        handler)))

(defn exception-logger [handler]
  (fn [{:keys [uri] :as request}]
    (try (handler request)
         (catch Exception e
           (error e (str "Error occured in " uri))
           (-> (response/response "Something bad happened")
               (response/status 500))))))

(defn make-req-log-msg [request]
  (str "Request " (select-keys request
                               [:body
                                :params
                                :uri
                                :headers
                                :request-method])))

(defn make-response-log-msg [response]
  (str "Response " response))

(defn request-response-logger [handler]
  (fn [request]
    (info (make-req-log-msg request))
    (let [response (handler request)]
      (info (make-response-log-msg response))
      response)))
