(ns winter-onboarding-2021.fleet-management-service.handlers.user
  (:require [ring.util.response :as response]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.views.user :as view]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-model]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]))

(def error-flash
  {:flash {:error true
           :style-class "alert alert-danger"
           :message "Could not create user, enter valid details!"}})

(def success-flash
  {:flash {:success true
           :style-class "alert alert-success"
           :message "User created successfully!"}})

(defn signup-form [_]
  {:title "Sign-up"
   :content (view/signup-form)})

(defn create-user [{:keys [multipart-params]}]
  (let [validated-user (s/conform ::specs/signup-form multipart-params)
        _ (println "valid" validated-user)]
    (if (s/invalid? validated-user)
      (-> error-flash
          (assoc-in [:flash :data] multipart-params)
          (merge (response/redirect "/users/signup")))
      (let [_ (user-model/create (assoc validated-user :users/role "admin"))]
        (merge success-flash (response/redirect "/users/signup"))))))
