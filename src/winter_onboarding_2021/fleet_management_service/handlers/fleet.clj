(ns winter-onboarding-2021.fleet-management-service.handlers.fleet
  (:require [winter-onboarding-2021.fleet-management-service.models.fleet :as fleet-model]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [ring.util.response :as response]))


(def success-flash
  {:flash {:success true
           :style-class "alert alert-success"
           :message "Fleet created successfully!"}})
(def error-flash
  {:flash {:error true
           :style-class "alert alert-danger"
           :message "Could not create fleet, try again!"}})

(defn create-fleet [req]
  (let [user-id (get-in req [:user :users/id])
        fleet-name (get-in req [:form-params :name])
        fleet-data {:name fleet-name
                    :created-by user-id}
        valid-fleet-data (s/conform ::specs/create-fleet fleet-data)]
    (if (s/invalid? valid-fleet-data)
      (->  error-flash
           (merge (response/redirect "/fleets")))
      (let [fleet-id (:fleets/id (fleet-model/create fleet-data))]
        (->  success-flash
             (merge (response/redirect (format "/fleets/%s" (str fleet-id)))))))))
