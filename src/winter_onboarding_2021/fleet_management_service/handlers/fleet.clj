(ns winter-onboarding-2021.fleet-management-service.handlers.fleet
  (:require [clojure.spec.alpha :as s]
            [next.jdbc :as jdbc]
            [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-models]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.views.fleet :as views]
            [winter-onboarding-2021.fleet-management-service.models.fleet :as fleet-model]))


(defn create-fleet [{:keys [user form-params]}]
  (jdbc/with-transaction [tx db-core/db-conn]
    (let [{user-id :users/id org-id :users/org-id} user
          fleet-name (:name form-params)
          fleet-data (utils/namespace-keys :fleets {:name fleet-name
                                                    :created-by user-id
                                                    :org-id org-id})]
      (if (s/valid? ::specs/fleets fleet-data)
        (let [fleet (fleet-model/create-and-associate tx fleet-data user)
              fleet-id (:fleets/id fleet)]
          (merge (utils/flash-msg "Fleet created successfully!" true)
                 (response/redirect (format "/fleets/%s" (str fleet-id)))))
        (merge (utils/flash-msg "Could not create fleet, try again!" false)
               (response/redirect "/fleets/new"))))))

(defn new [{:keys [user]}]
  (if-let [org-id (:users/org-id user)]
    (let [org-team-members (user-models/members {:organisations/id org-id}
                                                ["manager" "driver"])]
      {:title "Create fleet"
       :content (views/create-fleet org-team-members)})
    (merge (utils/flash-msg "You must join an organisation first" false)
           (response/redirect "/users/dashboard"))))

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
          show-next-page? (<= (* current-page page-size) fleets-count)
          show-prev-page? (> current-page 1)]
      {:title "List of fleets"
       :content (views/show-fleets fleets-with-managers
                                   current-page
                                   show-prev-page?
                                   show-next-page?)})))
