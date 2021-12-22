(ns winter-onboarding-2021.fleet-management-service.factories
  (:require [clojure.spec.gen.alpha :as gen]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]))

(defn generate-cab [num]
  (s/gen (s/coll-of ::specs/cabs
                    :distinct :true
                    :count num)))

(defn create-cabs [num]
  (gen/generate (generate-cab num)))

(defn generate-user []
  (gen/generate (s/gen ::specs/signup-form)))
