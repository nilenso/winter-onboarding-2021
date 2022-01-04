(ns winter-onboarding-2021.fleet-management-service.db.user-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.user :as user-db]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.factories :as factories])
  (:import [org.postgresql.util PSQLException]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-user
  (testing "Should create a user in the users table"
    (let [_ (user-db/create {:name "Harry Potter"
                             :role "admin"
                             :email "harry@hogwarts.edu"
                             :password "hermione@123"})]
      (is (= #:users{:name "Harry Potter"
                     :role "admin"
                     :email "harry@hogwarts.edu"
                     :password "hermione@123"}
             (select-keys
              (first (user-db/find-by-keys {:email "harry@hogwarts.edu"}))
              [:users/name :users/role :users/email :users/password])))))
  (testing "Should throw an exception, given name is nil"
    (is (= {:error :validation-failed}
           (user-db/create {:name nil
                            :role "admin"
                            :email "harry@hogwarts.edu"
                            :password "lily"}))))
  (testing "Should throw an exception, given email is nil"
    (is (= {:error :validation-failed}
           (user-db/create {:name "Harry"
                            :role "admin"
                            :email nil
                            :password "lily"}))))
  (testing "Should throw an exception, if given email already exists in db"
    (is (thrown-with-msg? PSQLException
                          #"Detail: Key \(email\)=\(harry@hogwarts.edu\) already exists."
                          (user-db/create {:name "Harry Potter"
                                           :role "admin"
                                           :email "harry@hogwarts.edu"
                                           :password "lily"})))))

(deftest find-by-email
  (testing "Should return a user given an email in key-map(properties)"
    (let [admin (factories/admin {:name "Something"
                                  :email "foobar@baz.com"})
          admin-2 (factories/admin {:name "Something 2"
                                    :email "foobar@baz2.com"})]
      (user-db/create admin)
      (user-db/create admin-2)
      (is (= [admin] (map #(select-keys % [:users/name :users/email :users/role :users/password])
                          (user-db/find-by-keys {:email (:users/email admin)})))))))

(deftest find-by-role
  (testing "Should return a user given a role in key-map(properties)"
    (let [admins  (repeatedly 2 factories/admin)
          managers (repeatedly 2 factories/manager)]
      (doall (map user-db/create admins))
      (doall (map user-db/create managers))
      (is (= 2 (count (user-db/find-by-keys {:role "manager"})))))))

(deftest find-by-name
  (testing "Should return a user given a name in key-map(properties)"
    (let [users (repeatedly 3 factories/user)
          name "Same name"
          user-1 (factories/user {:name name})
          user-2 (factories/user {:name name})]
      (doall (map user-db/create users))
      (user-db/create user-1)
      (user-db/create user-2)
      (is (= 2 (count (user-db/find-by-keys {:name name})))))))
