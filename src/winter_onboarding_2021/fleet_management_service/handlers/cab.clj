(ns winter-onboarding-2021.fleet-management-service.handlers.cab
  (:require [ring.util.response :as response]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.models.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]
            [winter-onboarding-2021.fleet-management-service.views.cab :as cab-views]))

(s/def :cab/name string?)
(s/def :cab/licence_plate string?)

(s/def :cab/distance_travelled
  (s/conformer
   #(try (Long/parseLong %)
         (catch Exception _ :clojure.spec.alpha/invalid))
   str))

(s/def ::create-cab-request
  (s/keys :req-un [:cab/name
                   :cab/licence_plate
                   :cab/distance_travelled]))

(def error-flash
  {:flash {:error true
           :style-class "alert alert-danger"
           :message "Could not add cab, try again!"}})
(def success-flash
  {:flash {:success true
           :style-class "alert alert-success"
           :message "Cab added successfully!"}})

(defn add [{:keys [multipart-params] :as request}]
  (let [validated-cab (s/conform ::create-cab-request
                                 multipart-params)]
    (if (s/invalid? validated-cab)
      (merge (assoc-in error-flash
                       [:flash :data] validated-cab)
             (response/redirect "/cabs/new"))
      (do (cab/create validated-cab)
          (merge success-flash (response/redirect "/cabs/new"))))))

(defn serve-add-cab-form [request]
  (response/response
   (layout/application
    request
    "Add a cab"
    (cab-views/add-cab-form
     (get-in request [:flash :data])))))
