(ns winter-onboarding-2021.fleet-management-service.handlers.invites
  (:require [clojure.spec.alpha :as s]
            [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.views.invite :as invites-view]
            [winter-onboarding-2021.fleet-management-service.models.invite :as invite-model]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))

(defn invites-page [req]
  (let [invites (invite-model/find-by-keys {:invites/created-by (get-in req [:user :users/id])})]
    {:title "List Invites"
     :content (invites-view/invites-page invites)}))

(defn- invite-create-params [{:keys [user form-params]}]
  (assoc (utils/namespace-keys :invites form-params)
         :invites/created-by
         (:users/id user)))

(defn create [req]
  (let [invite-params (s/conform ::specs/invites-create-form (invite-create-params req))]
    (if (s/invalid? invite-params)
      (-> (utils/flash-msg "Invalid parameters sent, try again" false)
          (merge (response/redirect "/invites/new")))
      (let [resp (invite-model/create invite-params)]
        (cond (= (:error resp)
                 :generic-error) (-> (utils/flash-msg "Some error has occured, please try again." false)
                                     (merge (response/redirect "/invites/new")))
              (= (:error resp)
                 :validation-failed) (-> (utils/flash-msg "Some error has occured, please try again." false)
                                         (merge (response/redirect "invites/new")))
              :else (-> (utils/flash-msg (str "Invite created successfully. <br> Link - "
                                              (config/get-host-url)
                                              "/users/signup?token="
                                              (:invites/token resp))
                                         true)
                        (merge (response/redirect "/invites/new"))))))))
