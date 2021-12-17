(ns winter-onboarding-2021.fleet-management-service.handlers.user
  (:require [ring.util.response :as response]
            [clojure.spec.alpha :as s]
            [crypto.password.bcrypt :as password]
            [winter-onboarding-2021.fleet-management-service.session :as session]
            [winter-onboarding-2021.fleet-management-service.views.user :as view]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-model]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]))

(defn flash-msg [msg success?]
  (if success?
    {:flash {:success true
             :style-class "alert alert-success"
             :message msg}}
    {:flash {:error true
             :style-class "alert alert-danger"
             :message msg}}))

(defn signup-form [_]
  {:title "Sign-up"
   :content (view/signup-form)})

(defn user-exist? [email]
  (not-empty (user-model/find-by-keys {:email email})))

(defn uuid []
  (java.util.UUID/randomUUID))

(defn create-user [{:keys [form-params]}]
  (let [validated-user (s/conform ::specs/signup-form form-params)]
    (cond
      (s/invalid? validated-user) (merge (flash-msg "Could not create user, enter valid details!" false)
                                         (response/redirect "/users/signup"))
      (user-exist? (:email form-params)) (merge (flash-msg "User already exisits, use different email!" false)
                                                (response/redirect "/users/signup"))
      :else (let [created-user (user-model/create (assoc validated-user
                                                         :password (password/encrypt
                                                                    (:password validated-user))))]
              (merge (flash-msg (format "User %s created successfully!" (:users/name created-user)) true)
                     (response/redirect "/users/signup"))))))

(defn login-form [_]
  {:title "Login"
   :content (view/login-form)})

(defn- successful-login-response [user-id]
  (let [session-id (uuid)]
    (session/write-session session-id user-id)
    (merge (flash-msg "Hooray! Logged in!" true)
           (-> (response/redirect "/users/dashboard")
               (response/set-cookie "session-id" session-id {:path "/"})))))

(defn- wrong-password-response []
  (merge (flash-msg "Wrong password" false)
         (response/redirect "/users/login")))

(defn- email-not-found-response []
  (merge (flash-msg "User with email not found" false)
         (response/redirect "/users/login")))

(defn- invalid-data-response []
  (merge (flash-msg "Please send valid data" false)
         (response/redirect "/users/login")))

(defn login [{:keys [params]}]
  (let [validated-params (s/conform ::specs/login-params params)]
    (if (s/invalid? validated-params)
      (invalid-data-response)
      (if-let [db-user (first (user-model/find-by-keys (select-keys validated-params [:email])))]
        (if (password/check (:password validated-params) (:users/password db-user))
          (successful-login-response (:users/id db-user))
          (wrong-password-response))
        (email-not-found-response)))))

(defn not-authorized [_]
  (merge (flash-msg "You are not authorized" false)
         (response/redirect "/users/dashboard")))

(defn not-logged-in [_]
  (merge (flash-msg "You are not logged in" false)
         (response/redirect "/users/login")))
