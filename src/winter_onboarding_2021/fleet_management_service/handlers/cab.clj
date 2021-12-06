(ns winter-onboarding-2021.fleet-management-service.handlers.cab
  (:require [ring.util.response :as response]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.views.cab :as cab-views]
            [winter-onboarding-2021.fleet-management-service.models.cab :as cab-model]
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
      (do (cab-model/create validated-cab)
          (merge success-flash (response/redirect "/cabs/new"))))))

(defn new [request]
  (cab-views/cab-form
   (get-in request [:flash :data])))

(defn get-cabs [req]
  (try (let [{:keys [page]} (:params req)
             page-size (config/get-page-size)
             page-int (Integer/parseInt (or page "1"))
             offset (* page-size (- page-int 1)) ;;offset is 0 for for page 1
             cabs (cab-model/select offset
                                    page-size)
             rows-count (:count (first (cab-model/cabs-count)))
             show-next-page? (<= (* page-int page-size) rows-count)]
         (cab-views/show-cabs cabs
                              page-int
                              show-next-page?))
       (catch Exception e
         (str "Enter valid page number or something went wrong. <br>"
              (.getMessage e)))))
