(ns winter-onboarding-2021.fleet-management-service.handlers.cab
  (:require [ring.util.response :as response]
            [ring.middleware.params :as params]
            [clojure.spec.alpha :as s]
            [clojure.walk :as walk]
            [aero.core :refer (read-config)]
            [winter-onboarding-2021.fleet-management-service.db.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]
            [winter-onboarding-2021.fleet-management-service.views.cab :as cab-view]))

(def page-size ((read-config "config/config.edn") :default-page-limit))

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

(defn add [{:keys [multipart-params] :as request}]
  (let [validated-cab (s/conform ::create-cab-request
                                 multipart-params)]
    (if (s/invalid? validated-cab)
      (-> (response/response "missing some fields")
          (response/status 400))
      (do (cab/create validated-cab)
          (response/redirect "/cabs/new")))))

(defn get-cabs [req]
  (try (let [{:keys [page]} (:query-params (-> req
                                               params/params-request
                                               walk/keywordize-keys))
             page-int (Integer/parseInt (or page "1"))
             offset (* page-size (- page-int 1)) ;;offset is 0 for for page 1
             cabs (cab/select offset
                              page-size)
             rows-count (:count ((cab/cabs-count) 0))
             show-next-page? (<= (* page-int page-size) rows-count)]
         (cab-view/show-cabs cabs 
                             page-int 
                             show-next-page?))
       (catch Exception e
         (str "Enter valid page number or something went wrong. <br>"
              (.getMessage e)))))
