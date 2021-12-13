(ns winter-onboarding-2021.fleet-management-service.handlers.cab
  (:require [ring.util.response :as response]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.models.cab :as models]
            [winter-onboarding-2021.fleet-management-service.views.cab :as views]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]))

(defn flash-msg [msg success?]
  (if success?
   {:flash {:success true
            :style-class "alert alert-success"
            :message msg}}
    {:flash {:error true
             :style-class "alert alert-danger"
             :message msg}}))

(defn string->uuid [id]
  (try
    (java.util.UUID/fromString id)
    (catch Exception _ nil)))

(def error-flash
  {:flash {:error true
           :style-class "alert alert-danger"
           :message "Could not add cab, try again!"}})

(def update-error-flash
  {:flash {:error true
           :style-class "alert alert-danger"
           :message "Could not update cab, try again!"}})

(def success-flash
  {:flash {:success true
           :style-class "alert alert-success"
           :message "Cab added successfully!"}})

(def update-success-flash
  {:flash {:success true
           :style-class "alert alert-success"
           :message "Cab updated successfully!"}})

(defn- view-cab-response [{:cabs/keys [name] :as cab}]
  (if cab
    {:title (str "Cab - " name)
     :content (views/cab cab)}
    {:title "No cabs found"
     :content (views/cab-not-found)}))

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
  (let [id-or-licence (get-in request [:params :slug])]
    (if-let [id (string->uuid id-or-licence)]
      (view-cab-response (models/get-by-id id))
      (view-cab-response (models/get-by-licence-plate id-or-licence)))))

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

(defn update-cab [req]
  (let [cab (:multipart-params req)
        id (get-in req [:params :slug])
        validated-cab (s/conform ::specs/update-cab-form cab)]
    (if (s/invalid? validated-cab)
      (-> update-error-flash
          (assoc-in [:flash :data] cab)
          (merge (response/redirect "/cabs/new")))
      (do
        (models/update! (string->uuid id) validated-cab)
        (merge update-success-flash
               (response/redirect (format "/cabs/%s" id)))))))

(defn update-cab-view [req]
  (let [cab (models/get-by-id (string->uuid (get-in req 
                                                    [:params :slug])))]
    {:title (str "Update cab " (:cabs/licence-plate cab))
     :content (views/update-cab-form cab)}))

(defn delete [request]
  (let [id (get-in request [:params :id])]
    (if (> (:next.jdbc/update-count (models/delete-by-id id)) 0)
      (-> (flash-msg "Cab deleted successfully" true)
          (merge (response/redirect "/cabs")))
      (-> (flash-msg "Cab does not exist" false)
          (merge (response/redirect "/cabs"))))))
