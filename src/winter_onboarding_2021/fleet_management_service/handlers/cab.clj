(ns winter-onboarding-2021.fleet-management-service.handlers.cab
  (:require [ring.util.response :as response]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.models.cab :as models]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]
            [winter-onboarding-2021.fleet-management-service.views.cab :as views]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]))

(def error-flash
  {:flash {:error true
           :style-class "alert alert-danger"
           :message "Could not add cab, try again!"}})
(def success-flash
  {:flash {:success true
           :style-class "alert alert-success"
           :message "Cab added successfully!"}})

(defn create [{:keys [multipart-params]}]
  (let [validated-cab (s/conform ::specs/create-cab-request
                                 multipart-params)]
    (if (s/invalid? validated-cab)
      (-> error-flash
          (assoc-in [:flash :data] multipart-params)
          (merge (response/redirect "/cabs/new")))
      (let [created-cab (models/create validated-cab)]
        (merge success-flash (response/redirect
                              (format
                               "/cabs/%s"
                               (:cabs/id created-cab))))))))

(defn new [request]
  (response/response
   (layout/application
    request
    "Add a cab"
    (views/cab-form
     (get-in request [:flash :data])))))

(defn view-cab [request]
  (let [cab (models/get-by-id (get-in request [:params :id]))]
    (response/response
     (layout/application
      request
      (:cabs/name cab)
      (views/cab cab)))))
