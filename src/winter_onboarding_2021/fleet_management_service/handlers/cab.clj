(ns winter-onboarding-2021.fleet-management-service.handlers.cab
  (:require [ring.util.response :as response]
            [ring.middleware.params :as params]
            [clojure.spec.alpha :as s]
            [clojure.walk :as walk]
            [winter-onboarding-2021.fleet-management-service.db.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]
            [winter-onboarding-2021.fleet-management-service.views.cab :as cab-view]))

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

(defn get-cabs [res]
  (let [{:keys [offset limit]} (:query-params (-> res
                                                  params/params-request
                                                  walk/keywordize-keys))
        cabs (cab/select
              (if (nil? offset) 0 offset)
              (if (nil? limit) 10 limit))]
    (response/response (layout/application "Cabs"
                                           (cab-view/show-cabs cabs)))))
