(ns winter-onboarding-2021.fleet-management-service.middleware
  (:require [clojure.walk :as walk]
            [taoensso.timbre :as timbre]
            [ring.logger :as logger]
            [ring.middleware.stacktrace :as stacktrace]
            [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.session :as session]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-model]))

(defn keywordize-multipart-params [handler]
  (fn [{:keys [multipart-params] :as request}]
    (-> request
        (assoc :multipart-params
               (walk/keywordize-keys multipart-params))
        handler)))

(defn keywordize-form-params [handler]
  (fn [{:keys [form-params] :as request}]
    (-> request
        (assoc :form-params
               (walk/keywordize-keys form-params))
        handler)))

(defn keywordize-cookies-keys [handler]
  (fn [request]
    (-> request
        (update :cookies walk/keywordize-keys)
        handler)))

(defn wrap-exception-handler [handler]
  (fn [request]
    (try (handler request)
         (catch Exception _
           (-> (response/response "Something bad happened")
               (response/status 500))))))

(defn ring-timbre-logger [handler]
  (-> handler
      stacktrace/wrap-stacktrace-log
      (logger/wrap-with-logger {:log-fn (fn [{:keys [level throwable message]}]
                                          (if (some? throwable)
                                            (timbre/log level throwable message)
                                            (timbre/log level message)))})))

(defn append-user-to-request [handler]
  (fn [request]
    (if-let [session-id (get-in request [:cookies :session-id :value])]
      (let [user-id (session/lookup (java.util.UUID/fromString session-id))
            db-user (select-keys (first (user-model/find-by-keys {:id user-id}))
                                 [:users/id :users/name :users/role :users/email])
            new-request (-> request
                            (assoc :user db-user))]
        (-> new-request
            handler))
      (handler request))))
