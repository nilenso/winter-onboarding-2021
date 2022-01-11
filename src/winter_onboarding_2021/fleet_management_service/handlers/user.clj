(ns winter-onboarding-2021.fleet-management-service.handlers.user
  (:require [ring.util.response :as response]
            [clojure.spec.alpha :as s]
            [crypto.password.bcrypt :as password]
            [winter-onboarding-2021.fleet-management-service.session :as session]
            [winter-onboarding-2021.fleet-management-service.views.user :as view]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-model]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))

(defn signup-form [_]
  {:title "Sign-up"
   :content (view/signup-form)})

(defn user-exist? [email]
  (not-empty (user-model/find-by-keys {:users/email email})))


(defn create-user [{:keys [form-params]}]
  (let [ns-form-params (utils/namespace-users form-params)
        validated-user (s/conform ::specs/signup-form ns-form-params)]
    (cond
      (s/invalid? validated-user) (merge (utils/flash-msg "Could not create user, enter valid details!" false)
                                         (response/redirect "/users/signup"))
      (user-exist? (:email form-params)) (merge (utils/flash-msg "User already exists, use different email!" false)
                                                (response/redirect "/users/signup"))
      :else (let [created-user (user-model/create (assoc validated-user
                                                         :users/password (password/encrypt
                                                                    (:users/password validated-user))))]
              (merge (utils/flash-msg (format "User %s created successfully!" (:users/name created-user)) true)
                     (response/redirect "/users/signup"))))))

(defn login-form [_]
  {:title "Login"
   :content (view/login-form)})

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
  (let [ns-params (utils/namespace-users params)
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

(defn not-authorized [_]
  (merge (utils/flash-msg "You are not authorized" false)
         (response/redirect "/users/dashboard")))

(defn not-logged-in [_]
  (merge (utils/flash-msg "You are not logged in" false)
         (response/redirect "/users/login")))
