(ns winter-onboarding-2021.fleet-management-service.handlers.fleet
  (:require [clojure.spec.alpha :as s]
            [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.utils :refer [namespace-keys flash-msg]]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.models.fleet :as fleet-model]
            [winter-onboarding-2021.fleet-management-service.views.fleet :as views]))


(defn create-fleet [{:keys [user form-params]}]
  (let [user-id (:users/id user)
        fleet-name (:name form-params)
        fleet-data (namespace-keys :fleets {:name fleet-name
                                            :created-by user-id})]
    (if (s/valid? ::specs/fleets fleet-data)
      (let [fleet-id (:fleets/id (fleet-model/create fleet-data))]
        (->  (flash-msg "Fleet created successfully!" true)
             (merge (response/redirect (format "/fleets/%s" (str fleet-id))))))
      (->  (flash-msg "Could not create fleet, try again!" false)
           (merge (response/redirect "/fleets/new"))))))

(defn new [_]
  {:title "Create fleet"
   :content (views/create-fleet)})
