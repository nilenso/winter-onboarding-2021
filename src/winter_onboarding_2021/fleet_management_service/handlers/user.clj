(ns winter-onboarding-2021.fleet-management-service.handlers.user
  (:require [ring.util.response :as response]
            [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [crypto.password.bcrypt :as password]
            [clj-http.client :as client]
            [hiccup.page :refer [html5]]
            [winter-onboarding-2021.fleet-management-service.session :as session]
            [winter-onboarding-2021.fleet-management-service.views.user :as view]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-model]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.config :as config]))

(defn signup-form [req]
  (let [user (:user req)]
    (if (nil? user)
      (response/response (html5 (layout/application
                                 req
                                 "Sign up"
                                 (view/signup-form))))
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
 
(defn create-user [{:keys [form-params]}]
  (let [ns-form-params (utils/namespace-keys :users form-params)
        validated-user (s/conform ::specs/signup-form ns-form-params)
        g-recaptcha-response (:g-recaptcha-response form-params)]
    (cond
      (s/invalid? validated-user) (merge (utils/flash-msg "Could not create user, enter valid details!" false)
                                         (response/redirect "/users/signup"))
      (user-exist? (:email form-params)) (merge (utils/flash-msg "User already exists, use different email!" false)
                                                (response/redirect "/users/signup"))
      (empty? g-recaptcha-response) (merge (utils/flash-msg "Please complete reCAPTCHA" false)
                                           (response/redirect "/users/signup"))
      (recaptcha-invalid? g-recaptcha-response) (merge (utils/flash-msg "You are not a human." false)
                                                       (response/redirect "/users/signup"))
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
   
