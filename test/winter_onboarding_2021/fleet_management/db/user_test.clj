(ns winter-onboarding-2021.fleet-management.db.user-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.db.user :as user-db]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.utils :as utils])
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
             (utils/dissoc-irrelevant-keys-from-user
              (first (user-db/find-by-keys {:email "harry@hogwarts.edu"})))))))
  (testing "Should throw an exception, given name is nil"
    (is (thrown-with-msg? PSQLException
                          #"null value in column \"name\" of relation \"users\" violates not-null constraint"
                          (user-db/create {:name nil
                                           :role "admin"
                                           :email "harry@hogwarts.edu"
                                           :password "lily"}))))
  (testing "Should throw an exception, given email is nil"
    (is (thrown-with-msg? PSQLException
                          #"null value in column \"email\" of relation \"users\" violates not-null constraint"
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


(deftest find-by-keys
  (testing "Should return a user given a key-map(properties)"
    (let [_ (user-db/create {:name "Harry Potter"
                             :role "admin"
                             :email "harry@hogwarts.edu"
                             :password "hermione@123"})]
      (is (= #:users{:name "Harry Potter"
                     :role "admin"
                     :email "harry@hogwarts.edu"
                     :password "hermione@123"}
             (utils/dissoc-irrelevant-keys-from-user
              (first (user-db/find-by-keys {:email "harry@hogwarts.edu"}))))))))
