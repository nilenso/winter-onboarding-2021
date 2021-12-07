(ns winter-onboarding-2021.fleet-management.factories
  (:require [clojure.spec.gen.alpha :as gen]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]))

(defn generate-cab [] (gen/generate (s/gen ::specs/cabs)))

(defn create-cabs [num]
  (take num (repeatedly generate-cab)))
