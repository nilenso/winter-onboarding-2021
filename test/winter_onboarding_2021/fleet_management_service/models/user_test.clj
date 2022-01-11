(ns winter-onboarding-2021.fleet-management-service.models.user-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [crypto.password.bcrypt :as password]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-model]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-cab
  (testing "Should create a cab"
    (let [user {:users/name "Harry Potter"
                :users/role "admin"
                :users/email "harry@hogwarts.edu"
                :users/password "hermione@123"}
          _ (user-model/create user)]
      (is (= #:users{:name "Harry Potter"
                     :role "admin"
                     :email "harry@hogwarts.edu"
                     :password "hermione@123"}
             (select-keys (first (user-model/find-by-keys {:users/email (:users/email user)}))
                          [:users/name :users/role :users/email :users/password]))))))

(deftest find-by-keys
  (testing "Should return a user given a key-map(properties)"
    (let [user {:users/name "Harry Potter"
                :users/role "admin"
                :users/email "harry@hogwarts.edu"
                :users/password "hermione@123"}
          _ (user-model/create user)]
      (is (= #:users{:name "Harry Potter"
                     :role "admin"
                     :email "harry@hogwarts.edu"
                     :password "hermione@123"}
             (select-keys (first (user-model/find-by-keys {:users/email (:users/email user)}))
                          [:users/name :users/role :users/email :users/password]))))))

(deftest authenticate
  (testing "Correct login credentials, should return us user data"
    (let [user {:users/name "Harry Potter"
                :users/role "admin"
                :users/email "harry@hogwarts.edu"
                :users/password (password/encrypt "hermione@123")}
          created-user (user-model/create user)]

      (is (= {:found? true :user (dissoc created-user :users/password)}
             (dissoc (user-model/authenticate {:users/email (:users/email user)
                                               :users/password "hermione@123"})
                     :users/password)))))

  (testing "No email exists in the database"
    (is  (= {:found? false :user nil}
            (dissoc (user-model/authenticate {:users/email "foo@bar.com"
                                              :users/password "hermione@123"})
                    :users/password))))

  (testing "Password is wrong but the account is there"
    (is (= {:found? true :user nil}
           (dissoc (user-model/authenticate {:users/email "harry@hogwarts.edu"
                                             :users/password "wrongpassword"})
                   :users/password)))))
