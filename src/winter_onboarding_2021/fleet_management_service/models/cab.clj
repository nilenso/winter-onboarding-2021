(ns winter-onboarding-2021.fleet-management-service.models.cab
  (:require  [clojure.spec.alpha :as s]
             [winter-onboarding-2021.fleet-management-service.db.core :as db]
             [winter-onboarding-2021.fleet-management-service.models.core :as m-core]))

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

(defn create [cab]
  (m-core/insert! db/db-conn :cabs cab))
