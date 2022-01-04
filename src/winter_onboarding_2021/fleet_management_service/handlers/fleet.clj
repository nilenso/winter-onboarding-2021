(ns winter-onboarding-2021.fleet-management-service.handlers.fleet
  (:require [winter-onboarding-2021.fleet-management-service.models.fleet :as fleet-model]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.views.fleet :as views]))

(def success-flash
  {:flash {:success true
           :style-class "alert alert-success"
           :message "Fleet created successfully!"}})
(def error-flash
  {:flash {:error true
           :style-class "alert alert-danger"
           :message "Could not create fleet, try again!"}})

(defn create-fleet [{:keys [user form-params]}]
  (let [user-id (:users/id user)
        fleet-name (:name form-params)
        fleet-data {:name fleet-name
                    :created-by user-id}]
    (if (s/valid? ::specs/fleet-form fleet-data)
      (let [fleet-id (:fleets/id (fleet-model/create fleet-data))]
        (->  success-flash
             (merge (response/redirect (format "/fleets/%s" (str fleet-id))))))
      (->  error-flash
           (merge (response/redirect "/fleets/new"))))))

(defn new [_]
  {:title "Create fleet"
   :content (views/create-fleet)})
