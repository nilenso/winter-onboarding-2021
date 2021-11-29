(ns winter-onboarding-2021.fleet-management-service.handlers.cab
  (:require [ring.util.response :as response]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.models.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]
            [winter-onboarding-2021.fleet-management-service.views.cab :as cab-views]))

(def error-flash
  {:flash {:error true
           :style-class "alert alert-danger"
           :message "Could not add cab, try again!"}})
(def success-flash
  {:flash {:success true
           :style-class "alert alert-success"
           :message "Cab added successfully!"}})

(defn add [{:keys [multipart-params] :as request}]
  (let [validated-cab (s/conform ::cab/create-cab-request
                                 multipart-params)]
    (if (s/invalid? validated-cab)
      (-> error-flash
          (assoc-in [:flash :data] multipart-params)
          (merge (response/redirect "/cabs/new")))
      (do (cab/create validated-cab)
          (merge success-flash (response/redirect "/cabs/new"))))))

(defn serve-add-cab-form [request]
  (response/response
   (layout/application
    request
    "Add a cab"
    (cab-views/cab-form
     (get-in request [:flash :data])))))
