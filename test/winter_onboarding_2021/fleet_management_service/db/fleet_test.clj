(ns winter-onboarding-2021.fleet-management-service.db.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management-service.error :as error]
            [winter-onboarding-2021.fleet-management-service.db.fleet :as fleet-db]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-fleet
  (testing "Should create a fleet"
    (let [user-id (:users/id (factories/create :users (s/gen ::specs/users)))
          fleet (factories/create :fleets (gen/fmap #(assoc % :fleets/created-by user-id)
                                                    (s/gen ::specs/fleets)))]

      (is (= fleet (first (db-core/query! ["select * from fleets;"]))))))

  (testing "Should not create a fleet if create-by is not uuid"
    (let [user-id (:users/id (factories/create :users (s/gen ::specs/users)))
          fleet {:fleets/name "Azkaban Fleet 1"
                 :fleets/created-by (str user-id)}]
      (is (= error/validation-failed (select-keys (fleet-db/create fleet) [:error]))))))
