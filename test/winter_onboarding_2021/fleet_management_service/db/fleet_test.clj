(ns winter-onboarding-2021.fleet-management-service.db.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.db.fleet :as fleet-db]
            [winter-onboarding-2021.fleet-management-service.db.user :as user-db]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management-service.error :as error]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(defn dissoc-irrelevant-keys [fleet]
  (dissoc fleet :fleets/id :fleets/created-at))

(defn calc-offset [page-size page-number]
  (* page-size (dec page-number)))

(defn seed-user-fleets-db
  "adds `num-fleets` to the DB associated with a sample user"
  [num-fleets]
  (let [user #:users{:name "Hermione Granger"
              :role "admin"
              :email "hermione@hogwarts.edu"
              :password "weasley&potter@123"}
        user-id (:users/id (user-db/create user))
        fleets (vec (repeatedly num-fleets #(factories/fleet {:fleets/created-by user-id})))]
    (doall (map fleet-db/create fleets))
    {:user user :fleets fleets}))

(deftest create-fleet
  (testing "Should create a fleet"
    (let [user {:users/name "Hermione Granger"
                :users/role "admin"
                :users/email "hermione@hogwarts.edu"
                :users/password "weasley&potter@123"}
          user-id (:users/id (user-db/create user))
          fleet {:fleets/name "Azkaban Fleet 1"
                 :fleets/created-by user-id}]
      (fleet-db/create fleet)
      (is (= #:fleets{:name "Azkaban Fleet 1"
                      :created-by user-id} (select-keys (first (db-core/query! ["select * from fleets;"]))
                                                        [:fleets/created-by :fleets/name])))))
  (testing "Should not create a fleet if create-by is not uuid"
    (let [user {:users/name "Hermione Granger"
                :users/role "admin"
                :users/email "hermione@hogwart.edu"
                :users/password "weasley&potter@123"}
          user-id (:users/id (user-db/create user))
          fleet {:fleets/name "Azkaban Fleet 1"
                 :fleets/created-by (str user-id)}]
      (is (= error/validation-failed (select-keys (fleet-db/create fleet) [:error]))))))

(deftest select!
  (testing "Should fetch us a list of first 10 fleets regardless of the associatd user"
    (let [{:keys [fleets]} (seed-user-fleets-db 20)
          foo (fleet-db/select! 0 10)
          db-fleets (mapv dissoc-irrelevant-keys foo)]
      (def hi foo)
      (is (= (subvec fleets 0 10) db-fleets)))))

(deftest pagination
  (let [{:keys [fleets]} (seed-user-fleets-db 20)
        page-size 10]
    (testing "Should fetch a list of first 3 fleets regardless of the associatd user from the second page"
      (let [offset (calc-offset page-size 2)
            limit 3
            db-fleets (mapv dissoc-irrelevant-keys (fleet-db/select!
                                                    offset
                                                    limit))]
        (is (= (subvec fleets offset (+ offset limit)) db-fleets))))))
