(ns winter-onboarding-2021.fleet-management-service.handlers.cab
  (:require [ring.util.response :as response]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.models.cab :as models]
            [winter-onboarding-2021.fleet-management-service.views.cab :as views]
            [winter-onboarding-2021.fleet-management-service.config :as config]
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
  (let [validated-cab (s/conform ::specs/create-cab-form
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
  {:title "Add a cab"
   :content (views/cab-form
             (get-in request [:flash :data]))})

(defn view-cab [request]
  (let [cab (models/get-by-id (get-in request [:params :id]))]
    {:title (:cabs/name cab)
     :content (views/cab cab)}))

(defn get-cabs [req]
  (let [{:keys [page]} (:params req)
        page-size (config/get-page-size)
        current-page (Integer/parseInt (or page "1"))
        offset (* page-size (- current-page 1)) ;;offset is 0 for for page 1
        cabs (models/select offset
                            page-size)
        rows-count (models/cabs-count)
        show-next-page? (<= (* current-page page-size) rows-count)]
    {:title "List cabs"
     :content (views/show-cabs cabs
                               current-page
                               show-next-page?)}))
