(ns winter-onboarding-2021.fleet-management.factories
  (:require [clojure.spec.gen.alpha :as gen]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]))

(defn create-cabs [num]
  (gen/sample (s/gen ::specs/cabs) num))
