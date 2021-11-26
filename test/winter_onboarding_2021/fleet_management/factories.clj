(ns winter-onboarding-2021.fleet-management.factories
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))

(s/def :cabs/name #{"Tata" "Maruti" "Hyundai" "Mercedes" "BMW"})
(s/def :cabs/licence_plate (s/and string? #(<= (count %) 6) #(>= (count %) 1)))
(s/def :cabs/distance_travelled (s/int-in 800 10000))

(s/def :cabs/cab
  (s/keys :req
          [:cabs/name
           :cabs/licence_plate
           :cabs/distance_travelled]))

(defn generate-cab [] (gen/generate (s/gen :cabs/cab)))

(defn create-cabs [num]
  (take num (repeatedly generate-cab)))
