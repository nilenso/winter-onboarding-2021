(ns winter-onboarding-2021.fleet-management-service.middleware
  (:require [ring.util.response :as response]))

(defn wrap-exception-logging [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        (-> (response/response {:error "Internal Server Error"})
            (response/status 500))))))
