(ns winter-onboarding-2021.fleet-management-service.middleware
  (:require [clojure.walk :as walk]))

(defn keywordize-multipart-params [handler]
  (fn [{:keys [multipart-params] :as request}]
    (-> request
        (assoc :multipart-params
               (walk/keywordize-keys multipart-params))
        handler)))
