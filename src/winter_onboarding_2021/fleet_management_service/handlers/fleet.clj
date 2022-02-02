(ns winter-onboarding-2021.fleet-management-service.handlers.fleet
  (:require [clojure.spec.alpha :as s]
            [next.jdbc :as jdbc]
            [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-models]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.views.fleet :as views]
            [winter-onboarding-2021.fleet-management-service.models.fleet :as fleet-model]
            [winter-onboarding-2021.fleet-management-service.models.users-fleets :as users-fleets-models]))


(defn- wrap-in-vector [entity]
  (if (not (vector? entity))
    (vector entity)
    entity))

(defn create-fleet [{admin :user form-params :form-params}]
  (jdbc/with-transaction [tx db/db-conn]
    (let [{admin-id :users/id
           org-id :users/org-id} admin
          {fleet-name :name
           :keys [managers drivers]} form-params
          fleet-data #:fleets{:name fleet-name
                              :created-by admin-id
                              :org-id org-id}
          managers-uuids (map utils/string->uuid (wrap-in-vector managers))
          drivers-uuids (map utils/string->uuid (wrap-in-vector drivers))
          team-members (remove nil? `(~@managers-uuids ~@drivers-uuids))

          ;; If managers or drivers are empty, it's okay to create a fleet
          managers-in-org? (or (empty? managers)
                               (user-models/users-in-org? tx managers-uuids {:organisations/id org-id}))
          drivers-in-org?  (or (empty? drivers)
                               (user-models/users-in-org? tx drivers-uuids {:organisations/id org-id}))]
      
      (if (and (s/valid? ::specs/fleets fleet-data)
               managers-in-org?
               drivers-in-org?)
        (let [{fleet-id :fleets/id :as fleet} (fleet-model/create tx fleet-data)]
          (users-fleets-models/create tx admin fleet)
          (doall (map #(users-fleets-models/create tx {:users/id %} fleet)
                      team-members))
          (->  (utils/flash-msg "Fleet created successfully!" true)
               (merge (response/redirect (format "/fleets/%s" (str fleet-id))))))
        (->  (utils/flash-msg "Could not create fleet, try again!" false)
             (merge (response/redirect "/fleets/new")))))))

(defn new [{:keys [user]}]
  (if-let [org-id (:users/org-id user)]
    (let [org-team-members (user-models/members {:organisations/id org-id}
                                                ["manager" "driver"])]
      {:title "Create fleet"
       :content (views/create-fleet org-team-members)})
    (merge (utils/flash-msg "You must join an organisation first" false)
           (response/redirect "/users/dashboard"))))

(defn show-fleets [request]
  (jdbc/with-transaction [tx db/db-conn]
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
