(ns winter-onboarding-2021.fleet-management-service.specs
  (:require [clojure.spec.alpha :as s]))

(s/def :cabs/name (s/and string? #(< 3 (count %) 20)))
(s/def :cabs/licence-plate (s/and string? #(< 3 (count %) 15))) ;https://en.wikipedia.org/wiki/Vehicle_registration_plates_of_India
(s/def :cabs/distance-travelled (s/int-in 700 10000))
(s/def :cabs-form/distance-travelled (s/conformer
                                     #(try (Long/parseLong %)
                                           (catch Exception _ :clojure.spec.alpha/invalid))
                                     str))

(s/def ::create-cab-form
  (s/keys :req-un [:cabs/name
                   :cabs/licence-plate
                   :cabs-form/distance-travelled]))

(s/def ::cabs
  (s/keys :req [:cabs/name
                :cabs/licence-plate
                :cabs/distance-travelled]))
