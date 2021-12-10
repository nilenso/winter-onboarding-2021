(ns winter-onboarding-2021.fleet-management-service.specs
  (:require [clojure.spec.alpha :as s]))

(def licence-plate-regex #"^[a-zA-Z0-9]*$")

(s/def :cabs/name string?)
(s/def :cabs/licence-plate (s/and string? not-empty #(re-matches licence-plate-regex %)))
(s/def :cabs/distance-travelled (s/int-in 0 100000000000))
(s/def :cabs-form/distance-travelled (s/conformer
                                      #(try (Long/parseLong %)
                                            (catch Exception _ :clojure.spec.alpha/invalid))
                                      str))

(s/def ::create-cab-form
  (s/keys :req-un [:cabs/name
                   :cabs/licence-plate
                   :cabs-form/distance-travelled]))

(s/def ::update-cab-form
  (s/keys :req-un [:cabs/name
                   :cabs-form/distance-travelled]))

(s/def ::cabs
  (s/keys :req [:cabs/name
                :cabs/licence-plate
                :cabs/distance-travelled]))
