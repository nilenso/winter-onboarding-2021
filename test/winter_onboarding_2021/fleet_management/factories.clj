(ns winter-onboarding-2021.fleet-management.factories
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))


(s/def :cabs/name string?)
(s/def :cabs/licence_plate string?)
(s/def :cabs/distance_travelled number?)

(s/def :cabs/cab
  (s/keys :req
          [:cabs/name
           :cabs/licence_plate
           :cabs/distance_travelled]))

(gen/generate (s/gen :cabs/cab))


(defn create-cabs [num]
  [])