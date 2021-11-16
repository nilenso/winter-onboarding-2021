(ns winter-onboarding-2021.fleet-management-service.handler
  (:require [ring.util.response :as response]))

(def dummy-cabs
  [{:id 1
    :name "Cab 1"}
   {:id 2
    :name "Cab 2"}
   {:id 3
    :name "Cab 3"}
   {:id 4
    :name "Cab 4"}])

(defn fetch-all-cabs [request]
  (response/response dummy-cabs))
