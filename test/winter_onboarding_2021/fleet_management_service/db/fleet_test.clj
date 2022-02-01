(ns winter-onboarding-2021.fleet-management-service.db.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.db.fleet :as fleet-db]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.db.core :as core-db]
            [winter-onboarding-2021.fleet-management-service.error :as error]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(defn- dissoc-irrelevant-keys [fleet]
  (dissoc fleet :fleets/created-at :fleets/org-id))

(defn- select-keys-user [user]
  (select-keys user [:users/id
                     :users/name
                     :users/role
                     :users/email
                     :users/created-at
                     :users/updated-at
                     :users/org-id]))

(defn seed-user-fleets-db
  "adds `num-fleets` to the DB associated with a sample user"
  [num-fleets]
  (let [{user-id :users/id :as user} (factories/admin)
        {org-id :organisations/id} (factories/organisation {:organisations/created-by user-id})
        fleets (vec (factories/create-list :fleets
                                           num-fleets
                                           (factories/overridden-generator {:fleets/created-by user-id
                                                                            :fleets/org-id org-id}
                                                                           ::specs/fleets)))]
    (doall (map #(core-db/insert! :users_fleets {:user-id (:users/id user)
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
    (let [{user-id :users/id} (factories/create :users (s/gen ::specs/users))
          {org-id :organisations/id} (factories/organisation {:organisations/created-by user-id})
          fleet (factories/create :fleets (factories/overridden-generator {:fleets/created-by user-id
                                                                           :fleets/org-id org-id}
                                                                          ::specs/fleets))]

      (is (= fleet (first (core-db/query! ["select * from fleets;"]))))))

  (testing "Should not create a fleet if create-by is not uuid"
    (let [user-id (:users/id (factories/create :users (s/gen ::specs/users)))
          fleet {:fleets/name "Azkaban Fleet 1"
                 :fleets/created-by (str user-id)}]
      (is (= error/validation-failed
             (select-keys (fleet-db/create core-db/db-conn fleet) [:error]))))))

(deftest user-fleets
  (testing "Should fetch us a list of first 10 fleets related to an admin user"
    (let [{:keys [user fleets]} (seed-user-fleets-db 5)
          _ (seed-user-fleets-db 5)
          db-fleets (fleet-db/user-fleets core-db/db-conn (:users/id user) 0 10)]
      (is (= 5 (count fleets)))
      (is (= (map dissoc-irrelevant-keys fleets)
             (map dissoc-irrelevant-keys db-fleets))))))

(deftest pagination
  (let [{:keys [user fleets]} (seed-user-fleets-db 20)
        page-size 10]
    (testing "Should fetch a list of first 3 fleets regardless of the associatd user from the second page"
      (let [offset (utils/offset page-size 2)
            limit 3
            db-fleets (fleet-db/user-fleets core-db/db-conn (:users/id user) offset limit)]
        (is (= (mapv dissoc-irrelevant-keys (subvec fleets offset (+ offset limit)))
               (mapv dissoc-irrelevant-keys db-fleets)))))))

(deftest managers
  (testing "Should get us list of managers associated with a certain fleet"
    (let [{:keys [user fleets]} (seed-user-fleets-db 1)
          manager1 (dissoc (factories/manager) :users/password :users/invite-id)
          manager2 (dissoc (factories/manager) :users/password :users/invite-id)]
      (core-db/insert! :users-fleets {:user-id (:users/id user)
                                      :fleet-id (:fleets/id (first fleets))})
      (core-db/insert! :users-fleets {:user-id (:users/id manager1)
                                      :fleet-id (:fleets/id (first fleets))})
      (core-db/insert! :users-fleets {:user-id (:users/id manager2)
                                      :fleet-id (:fleets/id (first fleets))})
      (is (= (mapv select-keys-user [manager1 manager2])
             (fleet-db/managers core-db/db-conn (first fleets)))))))
