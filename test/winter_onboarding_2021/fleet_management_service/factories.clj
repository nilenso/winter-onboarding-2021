(ns winter-onboarding-2021.fleet-management-service.factories
  (:require [clojure.spec.gen.alpha :as gen]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]))

(defn overridden-generator [overrides spec]
  (gen/fmap #(merge % overrides)
            (s/gen spec)))

(defn build [generator]
  (gen/generate generator))

(defn create [table generator]
  (db-core/insert! table (gen/generate generator)))

(defn create-list [table count generator]
  (doall (map #(db-core/insert! table %)
              (gen/sample generator count))))

(defn generate-cab [num]
  (s/gen (s/coll-of ::specs/cabs
                    :distinct :true
                    :count num)))

(defn create-cabs [num]
  (gen/generate (generate-cab num)))

(defn user
  ([] (user {}))
  ([overrides] (merge (gen/generate (s/gen ::specs/users)) overrides)))

(defn admin
  ([] (admin {}))
  ([overrides] (create :users (gen/fmap #(merge % overrides {:users/role "admin"})
                                        (s/gen ::specs/users)))))

(defn manager
  ([] (manager {}))
  ([overrides] (create :users (gen/fmap #(merge % overrides {:users/role "manager"})
                                        (s/gen ::specs/users)))))
