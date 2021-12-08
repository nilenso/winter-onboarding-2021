(ns winter-onboarding-2021.fleet-management-service.middleware
  (:require [clojure.walk :as walk]
            [taoensso.timbre :refer [error]]
            [ring.util.response :as response]))

(defn keywordize-multipart-params [handler]
  (fn [{:keys [multipart-params] :as request}]
    (-> request
        (assoc :multipart-params
               (walk/keywordize-keys multipart-params))
        handler)))

(defn exception-logging [handler]
  (fn [{:keys [uri] :as request}]
    (try (handler request)
         (catch Exception e
           (error e (str "Error occured in " uri))
           (-> (response/response "Something bad happened")
               (response/status 500))))))
