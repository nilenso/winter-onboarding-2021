(ns winter-onboarding-2021.fleet-management-service.db.user-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [clojure.spec.alpha :as s]
            [winter-onboarding-2021.fleet-management-service.db.user :as user-db]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.error :as errors]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.models.organisation :as org-models]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core])
  (:import [org.postgresql.util PSQLException]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-user
  (testing "Should create a user in the users table"
    (let [user #:users{:name "Harry Potter"
                       :role "admin"
                       :email "harry@hogwarts.edu"
                       :password "hermione@123"}
          _ (user-db/create user)]
      (is (= user
             (select-keys
              (first (user-db/find-by-keys {:email "harry@hogwarts.edu"}))
              [:users/name :users/role :users/email :users/password])))))

  (testing "Should throw an exception, given name is nil"
    (is (= errors/validation-failed
           (select-keys (user-db/create {:users/name nil
                                         :users/role "admin"
                                         :users/email "harry@hogwarts.edu"
                                         :users/password "lily"}) [:error]))))

  (testing "Should throw an exception, given email is nil"
    (is (= errors/validation-failed
           (select-keys (user-db/create {:users/name "Harry"
                                         :users/role "admin"
                                         :users/email nil
                                         :users/password "lily"}) [:error]))))

  (testing "Should throw an exception, if given email already exists in db"
    (is (thrown-with-msg? PSQLException
                          #"Detail: Key \(email\)=\(harry@hogwarts.edu\) already exists."
                          (user-db/create {:users/name "Harry Potter"
                                           :users/role "admin"
                                           :users/email "harry@hogwarts.edu"
                                           :users/password "lily"})))))

(deftest find-by-email
  (testing "Should return a user given an email in key-map(properties)"
    (let [admin (factories/admin {:users/name "Something"
                                  :users/email "foobar@baz.com"})
          _ (factories/admin {:users/name "Something 2"
                              :users/email "foobar@baz2.com"})]
      (is (= [admin] (user-db/find-by-keys {:email (:users/email admin)}))))))

(deftest find-by-role
  (testing "Should return a user given a role in key-map(properties)"
    (factories/create-list :users 2 (factories/overridden-generator {:users/role "admin"}
                                                                    ::specs/users))
    (factories/create-list :users 2 (factories/overridden-generator {:users/role "manager"}
                                                                    ::specs/users))
    (is (= 2 (count (user-db/find-by-keys {:role "manager"}))))))

(deftest find-by-name
  (testing "Should return a user given a name in key-map(properties)"
    (factories/create :users (s/gen ::specs/users))
    (factories/create-list :users 2 (factories/overridden-generator {:users/name "Same name"}
                                                                    ::specs/users))
    (is (= 2 (count (user-db/find-by-keys {:name "Same name"}))))))

(deftest add-to-org
  (testing "Should add org-id to an user"
    (let [admin (factories/admin)
          org (org-models/create db-core/db-conn {:name "org-1" :created-by (:users/id admin)})
          _ (user-db/add-to-org db-core/db-conn org admin)

          db-admin (first (user-db/find-by-keys {:id (:users/id admin)}))]

      (is (= (:organisations/id org) (:users/org-id db-admin))))))


(deftest members
  (let [admin (factories/admin)
        org (->> ::specs/organisations
                 (factories/overridden-generator {:organisations/created-by (:users/id admin)})
                 (factories/create :organisations))
        _ (user-db/add-to-org db-core/db-conn org admin)
        managers (->> ::specs/users
                      (factories/overridden-generator {:users/role "manager"
                                                       :users/org-id (:organisations/id org)})
                      (factories/create-list :users 2))
        drivers (->> ::specs/users
                     (factories/overridden-generator {:users/role "driver"
                                                      :users/org-id (:organisations/id org)})
                     (factories/create-list :users 2))]

    (testing "Should return us the members of given organisation & role"
      (is (= (user-db/find-by-keys {:id (:users/id admin)})
             (user-db/members org ["admin"])))
      (is (= managers (user-db/members org ["manager"])))
      (is (= drivers (user-db/members org ["driver"]))))

    (testing "Should not return us users who are not in any organisation"
      (let [non-member-managers (->> ::specs/users
                                     (factories/overridden-generator {:users/role "manager"})
                                     (factories/create-list :users 2))
            non-member-drivers (->> ::specs/users
                                    (factories/overridden-generator {:users/role "driver"})
                                    (factories/create-list :users 2))]
        (is (not= non-member-managers
                  (user-db/members org ["manager"])))
        (is (not= non-member-drivers
                  (user-db/members org ["driver"])))))

    (testing "Should not return us the members of another organisation"
      (let [another-admin (factories/admin)
            another-org (->> ::specs/organisations
                             (factories/overridden-generator {:organisations/created-by (:users/id another-admin)})
                             (factories/create :organisations))
            non-member-managers (->> ::specs/users
                                     (factories/overridden-generator {:users/role "manager"
                                                                      :users/org-id (:organisations/id another-org)})
                                     (factories/create-list :users 2))
            non-member-drivers (->> ::specs/users
                                    (factories/overridden-generator {:users/role "driver"
                                                                     :users/org-id (:organisations/id another-org)})
                                    (factories/create-list :users 2))]
        (is (not= non-member-managers
                  (user-db/members org ["manager"])))
        (is (not= non-member-drivers
                  (user-db/members org ["driver"])))))))
