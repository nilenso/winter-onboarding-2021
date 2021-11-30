(ns winter-onboarding-2021.fleet-management-service.middleware
  (:require [ring.util.response :as response]
            [clojure.walk :as walk]))

(defn wrap-exception-logging [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception _
        (-> (response/response {:error "Internal Server Error"})
            (response/status 500))))))

(defn keywordize-multipart-params [handler]
  (fn [{:keys [multipart-params] :as request}]
    (-> request
        (assoc :multipart-params
               (walk/keywordize-keys multipart-params))
        handler)))
