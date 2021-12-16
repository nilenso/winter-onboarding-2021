(ns winter-onboarding-2021.fleet-management.handlers.user-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.handlers.user :as handler]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-model]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest user-create
  (testing "Should create a user in db"
    (let [user {:name "Severus Snape"
                :email "s.snape@hogwarts.edu"
                :password "lily"}
          _ (handler/create-user {:multipart-params user})]
      (is (= #:users{:name "Severus Snape"
              :role "admin"
              :email "s.snape@hogwarts.edu"
              :password "lily"}
             (dissoc (first (user-model/find-by-keys {:email (:email user)})) :users/id)))))
  (testing "Should flash a message if user already exist"
    (let [user {:name "Severus Snape"
                :email "s.snape@hogwarts.edu"
                :password "lily"}
          response (handler/create-user {:multipart-params user})]
      (is (= {:error true
              :style-class "alert alert-danger"
              :message "User already exisits, use different email!"}
           (:flash response)))))
  (testing "Should flash a message invalid details are passed"
    (let [user {:name "Dumbledore"
                :email "albus@hogwarts"
                :password "fawkes"}
          response (handler/create-user {:multipart-params user})]
      (is (= {:error true
              :style-class "alert alert-danger"
              :message "Could not create user, enter valid details!"}
             (:flash response))))))
