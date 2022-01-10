(ns winter-onboarding-2021.fleet-management-service.db.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.db.fleet :as fleet-db]
            [winter-onboarding-2021.fleet-management-service.db.user :as user-db]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.db.core :as core-db]
            [winter-onboarding-2021.fleet-management-service.error :as error]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))
            

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(defn dissoc-irrelevant-keys [fleet]
  (dissoc fleet :fleets/created-at :fleets/org-id))

(defn seed-user-fleets-db
  "adds `num-fleets` to the DB associated with a sample user"
  [num-fleets]
  (let [user (user-db/create (factories/admin))
        fleets (doall (vec (repeatedly num-fleets
                                       #(fleet-db/create (factories/fleet {:fleets/created-by (:users/id user)})))))]
    (doall (map #(core-db/insert! :user_fleet {:user-id (:users/id user)
                                               :fleet-id (:fleets/id %)})
                fleets))
    {:user user :fleets fleets}))

(deftest total
  (testing "Count the total number of fleets in the DB"
    (let [total 20
          _ (seed-user-fleets-db total)
          db-total (:count (first (fleet-db/total)))]
      (is (= total db-total)))))

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
                      :created-by user-id} (select-keys (first (core-db/query! ["select * from fleets;"]))
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

(deftest select-fleets-for-admin
  (testing "Should fetch us a list of first 10 fleets related to an admin user"
    (let [{:keys [user fleets]} (seed-user-fleets-db 5)
          _ (seed-user-fleets-db 5)
          db-fleets (mapv dissoc-irrelevant-keys (fleet-db/select-fleets-for-admin (:users/id user) 0 10))]
      (is (= 5 (count fleets)))
      (is (= (map dissoc-irrelevant-keys fleets)
             db-fleets)))))

(deftest pagination
  (let [{:keys [user fleets]} (seed-user-fleets-db 20)
        page-size 10]
    (testing "Should fetch a list of first 3 fleets regardless of the associatd user from the second page"
      (let [offset (utils/calc-offset page-size 2)
            limit 3
            db-fleets (mapv dissoc-irrelevant-keys (fleet-db/select-fleets-for-admin
                                                    (:users/id user)
                                                    offset
                                                    limit))]
        (is (= (mapv dissoc-irrelevant-keys
                     (subvec fleets offset (+ offset limit)))
               db-fleets))))))

(deftest managers
  (testing "Should get us list of managers associated with a certain fleet"
    (let [{:keys [user fleets]} (seed-user-fleets-db 1)
          manager1 (dissoc (user-db/create (factories/manager)) :users/password)
          manager2 (dissoc (user-db/create (factories/manager)) :users/password)]
      (core-db/insert! :user-fleet {:user-id (:users/id user)
                                    :fleet-id (:fleets/id (first fleets))})
      (core-db/insert! :user-fleet {:user-id (:users/id manager1)
                                    :fleet-id (:fleets/id (first fleets))})
      (core-db/insert! :user-fleet {:user-id (:users/id manager2)
                                    :fleet-id (:fleets/id (first fleets))})
      (is (= [manager1 manager2] (fleet-db/managers (first fleets)))))))
