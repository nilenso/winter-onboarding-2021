(ns winter-onboarding-2021.fleet-management-service.handlers.user
  (:require [ring.util.response :as response]
            [next.jdbc :as jdbc]
            [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [crypto.password.bcrypt :as password]
            [clj-http.client :as client]
            [hiccup.page :refer [html5]]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [winter-onboarding-2021.fleet-management-service.session :as session]
            [winter-onboarding-2021.fleet-management-service.views.user :as view]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-model]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.models.invite :as invite]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]))

(defn signup-form [req]
  (let [user (:user req)
        token (get-in req [:params :token])]
    (if (nil? user)
      (response/response (html5 (layout/application
                                 req
                                 "Sign up"
                                 (view/signup-form token))))
      (response/redirect "/users/dashboard"))))

(defn- user-exist? [email]
  (not-empty (user-model/find-by-keys {:users/email email})))

(defn- recaptcha-invalid? [g-recaptcha-response]
  (try (let [res (client/post "https://www.google.com/recaptcha/api/siteverify"
                              {:form-params {:secret (config/recaptcha-secret)
                                             :response g-recaptcha-response}})
             body (json/read-str (:body res))]
         (false? (get-in body ["success"])))
       (catch Exception e
         (println "Could not verify reCAPTCHA" (.getMessage e))
         true)))

(defn- error-redirect [msg redirect-url]
  (merge (utils/flash-msg msg false)
         (response/redirect redirect-url)))

(defn- create-user-via-invite [token validated-user]
  (let [invite (first (invite/find-by-keys {:invites/token token}))]
    (cond
      (nil? invite) (error-redirect "Invite Token not found"
                                    "/users/signup")
      (t/after? (t/now) (c/from-sql-time (:invites/valid-until invite))) (error-redirect "Token expired."
                                                                                         "/users/signup")
      :else (jdbc/with-transaction [tx db-core/db-conn]
              (let [user (first (user-model/find-by-keys {:users/id (:invites/created-by invite)}))
                    org-id (:users/org-id user)
                    db-reponse (user-model/create tx
                                                  (assoc validated-user
                                                         :users/password (password/encrypt
                                                                          (:users/password validated-user))
                                                         :users/role (:invites/role invite)
                                                         :users/invite-id (:invites/id invite)
                                                         :users/org-id org-id))
                    invite-usage-count (count (user-model/find-by-keys tx {:users/invite-id (:invites/id invite)}))]
                (cond
                  (> invite-usage-count (:invites/usage-limit invite)) (do (.rollback tx)
                                                                           (error-redirect "Token usage limit reached. Please use a new token"
                                                                                           "/users/signup"))
                  (some? (:error db-reponse)) (error-redirect "Some error occured, please try again later."
                                                              "/users/signup")
                  :else (merge (utils/flash-msg (format "User %s created successfully in organisation %s"
                                                        (:users/name db-reponse)
                                                        (:users/org-id db-reponse))
                                                true)
                               (response/redirect "/users/signup"))))))))

(defn create-user [{:keys [form-params]}]
  (let [ns-form-params (utils/namespace-keys :users form-params)
        validated-user (s/conform ::specs/signup-form ns-form-params)
        g-recaptcha-response (:g-recaptcha-response form-params)
        token (:token form-params)]
    (cond
      (s/invalid? validated-user) (error-redirect "Could not create user, enter valid details!"
                                                  "/users/signup")
      (user-exist? (:email form-params)) (error-redirect "User already exists, use different email!"
                                                         "/users/signup")
      (empty? g-recaptcha-response) (error-redirect "Please complete reCAPTCHA"
                                                    "/users/signup")
      (recaptcha-invalid? g-recaptcha-response) (error-redirect "You are not a human."
                                                                "/users/signup")
      (some? token) (create-user-via-invite token validated-user)
      :else (let [created-user (user-model/create (assoc validated-user
                                                         :users/password (password/encrypt
                                                                          (:users/password validated-user))))]
              (merge (utils/flash-msg (format "User %s created successfully!" (:users/name created-user)) true)
                     (response/redirect "/users/signup"))))))

(defn login-form [req]
  (let [user (:user req)]
    (if (nil? user)
      (response/response (html5 (layout/application
                                 req
                                 "Login"
                                 (view/login-form))))
      (response/redirect "/users/dashboard"))))

(defn- successful-login-response [user-id]
  (let [session-id (session/new user-id)]
    (merge (utils/flash-msg "Hooray! Logged in!" true)
           (-> (response/redirect "/users/dashboard")
               (response/set-cookie "session-id" session-id {:path "/"})))))

(defn- wrong-password-response []
  (merge (utils/flash-msg "Wrong password" false)
         (response/redirect "/users/login")))

(defn- email-not-found-response []
  (merge (utils/flash-msg "User with email not found" false)
         (response/redirect "/users/login")))

(defn- invalid-data-response []
  (merge (utils/flash-msg "Please send valid data" false)
         (response/redirect "/users/login")))

(defn login [{:keys [params]}]
  (let [ns-params (utils/namespace-keys :users params)
        validated-params (s/conform ::specs/login-params ns-params)]
    (if (s/invalid? validated-params)
      (invalid-data-response)
      (let [{:keys [found? user]} (user-model/authenticate
                                   (select-keys validated-params [:users/email :users/password]))]
        (if found?
          (if user
            (successful-login-response (:users/id user))
            (wrong-password-response))
          (email-not-found-response))))))

(defn logout [req]
  (let [session-id (get-in req [:cookies :session-id :value])]
    (session/delete (java.util.UUID/fromString session-id))
    (merge (response/redirect "/")
           {:cookies {"session-id" {:value nil :path "/" :max-age 0}}})))

(defn not-authorized [_]
  (merge (utils/flash-msg "You are not authorized" false)
         (response/redirect "/users/dashboard")))

(defn not-logged-in [_]
  (merge (utils/flash-msg "You are not logged in" false)
         (response/redirect "/users/login")))
