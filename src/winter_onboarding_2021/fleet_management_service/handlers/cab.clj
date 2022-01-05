(ns winter-onboarding-2021.fleet-management-service.handlers.cab
  (:require [ring.util.response :as response]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.models.cab :as models]
            [winter-onboarding-2021.fleet-management-service.views.cab :as views]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))

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
      (-> (utils/flash-msg "Could not add cab, try again!" false)
          (assoc-in [:flash :data] multipart-params)
          (merge (response/redirect "/cabs/new")))
      (let [response (models/create validated-cab)]
        (cond (= (:error response)
                 :licence-plate-already-exists) (merge (utils/flash-msg "Cab with licence plate already exists" false)
                                                      (response/redirect "cabs/new"))
              (= (:error response)
                 :generic-error) (merge (utils/flash-msg "Some error occured" false)
                                                          (response/redirect "cabs/new"))
              :else (merge (utils/flash-msg "Cab added successfully!" true) (response/redirect
                                          (format
                                           "/cabs/%s"
                                           (:cabs/id response)))))))))

(defn new [request]
  {:title "Add a cab"
   :content (views/cab-form
             (get-in request [:flash :data]))})

(defn view-cab [{:keys [params]}]
  (if-let [slug (:slug params)]
    (view-cab-response (models/get-by-id-or-licence-plate slug))
    (response/redirect "/cabs")))

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
      (-> (utils/flash-msg "Could not update cab, try again!" false)
          (assoc-in [:flash :data] cab)
          (merge (response/redirect "/cabs/new")))
      (do
        (models/update! (utils/string->uuid id) validated-cab)
        (merge (utils/flash-msg "Cab updated successfully!" true)
               (response/redirect (format "/cabs/%s" id)))))))

(defn update-cab-view [{:keys [params]}]
  (let [cab (models/get-by-id-or-licence-plate (:slug params))]
    {:title (str "Update cab " (:cabs/licence-plate cab))
     :content (views/update-cab-form cab)}))

(defn delete [request]
  (let [id (get-in request [:params :id])]
    (if (= (:next.jdbc/update-count (models/delete-by-id (utils/string->uuid id))) 1)
      (-> (utils/flash-msg "Cab deleted successfully" true)
          (merge (response/redirect "/cabs")))
      (-> (utils/flash-msg "Cab does not exist" false)
          (merge (response/redirect "/cabs"))))))
