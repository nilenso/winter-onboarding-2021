(ns winter-onboarding-2021.fleet-management.models.user-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [crypto.password.bcrypt :as password]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-model]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-cab
  (testing "Should create a cab"
    (let [user {:name "Harry Potter"
                :role "admin"
                :email "harry@hogwarts.edu"
                :password "hermione@123"}
          _ (user-model/create user)]
      (is (= #:users{:name "Harry Potter"
                     :role "admin"
                     :email "harry@hogwarts.edu"
                     :password "hermione@123"}
             (utils/dissoc-irrelevant-keys-from-user
              (first (user-model/find-by-keys {:email (:email user)}))))))))

(deftest find-by-keys
  (testing "Should return a user given a key-map(properties)"
    (let [user {:name "Harry Potter"
                :role "admin"
                :email "harry@hogwarts.edu"
                :password "hermione@123"}
          _ (user-model/create user)]
      (is (= #:users{:name "Harry Potter"
                     :role "admin"
                     :email "harry@hogwarts.edu"
                     :password "hermione@123"}
             (utils/dissoc-irrelevant-keys-from-user
              (first (user-model/find-by-keys {:email (:email user)}))))))))

(deftest authenticate
  (testing "Correct login credentials, should return us user data"
    (let [user {:name "Harry Potter"
                :role "admin"
                :email "harry@hogwarts.edu"
                :password (password/encrypt "hermione@123")}
          created-user (user-model/create user)]

      (is (= {:found? true :user (dissoc created-user :users/password)}
             (dissoc (user-model/authenticate {:email (:email user)
                                               :password "hermione@123"})
                     :users/password)))))

  (testing "No email exists in the database"
    (is  (= {:found? false :user nil}
            (dissoc (user-model/authenticate {:email "foo@bar.com"
                                              :password "hermione@123"})
                    :users/password))))

  (testing "Password is wrong but the account is there"
    (is (= {:found? true :user nil}
           (dissoc (user-model/authenticate {:email "harry@hogwarts.edu"
                                             :password "wrongpassword"})
                   :users/password)))))
