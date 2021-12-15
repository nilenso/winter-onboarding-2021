(ns winter-onboarding-2021.fleet-management-service.handlers.user
  (:require [ring.util.response :as response]
            [clojure.spec.alpha :as s]
            [crypto.password.bcrypt :as password]
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
