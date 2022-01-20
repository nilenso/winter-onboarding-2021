(ns winter-onboarding-2021.fleet-management-service.handlers.fleet
  (:require [clojure.spec.alpha :as s]
            [next.jdbc :as jdbc]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.views.fleet :as views]
            [winter-onboarding-2021.fleet-management-service.models.fleet :as fleet-model]))


(defn create-fleet [{:keys [user form-params]}]
  (let [user-id (:users/id user)
        fleet-name (:name form-params)
        fleet-data (utils/namespace-keys :fleets {:name fleet-name
                                                  :created-by user-id})]
    (if (s/valid? ::specs/fleets fleet-data)
      (jdbc/with-transaction [tx db-core/db-conn]
        (let [fleet (fleet-model/create-and-associate tx fleet-data user)
              fleet-id (:fleets/id fleet)]
          (merge (utils/flash-msg "Fleet created successfully!" true)
                 (response/redirect (format "/fleets/%s" (str fleet-id))))))
      (merge (utils/flash-msg "Could not create fleet, try again!" false)
             (response/redirect "/fleets/new")))))

(defn new [_]
  {:title "Create fleet"
   :content (views/create-fleet)})

(defn show-fleets [request]
  (jdbc/with-transaction [tx db-core/db-conn]
    (let [user-id (get-in request [:user :users/id])
          {:keys [page]} (:params request)
          page-size (config/get-page-size)
          current-page (Integer/parseInt (or page "1"))
          offset (utils/offset page-size current-page)
          fleets-with-managers (fleet-model/fleets-with-managers tx
                                                                 user-id
                                                                 offset
                                                                 page-size)
          fleets-count (fleet-model/total)
          show-next-page? (<= (* current-page page-size) fleets-count)]
      {:title "List of fleets"
       :content (views/show-fleets fleets-with-managers
                                   current-page
                                   show-next-page?)})))
