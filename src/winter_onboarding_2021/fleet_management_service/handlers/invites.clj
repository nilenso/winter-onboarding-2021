(ns winter-onboarding-2021.fleet-management-service.handlers.invites
  (:require [clojure.spec.alpha :as s]
            [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.views.invite :as view]
            [winter-onboarding-2021.fleet-management-service.models.invite :as model]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))

(defn invites-page [req]
  (if (nil? (:user req))
    {:error :user-not-found-in-request}
    (let [invites (model/find-by-keys {:invites/created-by (get-in req [:user :users/id])})]
      {:title "List Invites"
       :content (view/invites-page invites)})))

(defn- link-from-token-msg [host token]
  (let [link (str host
                  "/users/signup?token="
                  token)]
    (str "Invite created successfully. <br> Link - <a href="
         link
         ">"
         link
         "</a>")))

(defn- invite-create-params [{:keys [user form-params]}]
  (assoc (utils/namespace-keys :invites form-params)
         :invites/created-by
         (:users/id user)))

(defn- get-host [req]
  (get-in req [:headers "host"]))

(defn create [req]
  (let [invite-params (s/conform ::specs/invites-create-form (invite-create-params req))
        host (get-host req)]
    (if (s/invalid? invite-params)
      (-> (utils/flash-msg "Invalid parameters sent, try again" false)
          (merge (response/redirect "/invites/new")))
      (let [resp (model/create invite-params)
            err (:error resp)]
        (condp = err
          :generic-error (merge (response/redirect "/invites/new")
                                (utils/flash-msg "Some error has occured, please try again." false))
          :validation-failed (merge (response/redirect "invites/new")
                                    (utils/flash-msg "Some error has occured, please try again." false))
          nil (merge (response/redirect "/invites/new")
                     (utils/flash-msg (link-from-token-msg host (:invites/token resp))
                                      true)))))))
