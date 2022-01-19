(ns winter-onboarding-2021.fleet-management-service.handlers.user-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [crypto.password.bcrypt :as password]
            [hiccup.page :refer [html5]]
            [winter-onboarding-2021.fleet-management-service.handlers.user :as handler]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-model]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.views.user :as user-view]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.db.user :as user-db]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest user-create
  (testing "Should create a user in db"
    (let [user {:name "Severus Snape"
                :email "s.snape@hogwarts.edu"
                :role "admin"
                :password "lily"}
          response (handler/create-user {:form-params user})
          created-user (first (user-model/find-by-keys {:users/email (:email user)}))]

      (is (= 302 (:status response)))

      (is (= {:success true
              :style-class "alert alert-success"
              :message (format "User %s created successfully!" (:users/name created-user))}
             (:flash response)))

      (is (= #:users{:name "Severus Snape"
                     :role "admin"
                     :email "s.snape@hogwarts.edu"}
             (select-keys created-user
                     [:users/name
                     :users/role
                     :users/email])))
      (is (password/check "lily" (:users/password created-user)))))

  (testing "Should flash a message if user already exist"
    (let [user {:name "Severus Snape"
                :email "s.snape@hogwarts.edu"
                :role "admin"
                :password "lily"}
          response (handler/create-user {:form-params user})]
      (is (= {:error true
              :style-class "alert alert-danger"
              :message "User already exists, use different email!"}
             (:flash response)))))
  (testing "Should flash a message invalid details are passed"
    (let [user {:name "Dumbledore"
                :email "albus@hogwarts"
                :password "fawkes"}
          response (handler/create-user {:form-params user})]
      (is (= {:error true
              :style-class "alert alert-danger"
              :message "Could not create user, enter valid details!"}
             (:flash response))))))

(deftest user-login
  (testing "Correct login credentials, should redirect to user dashboard"
    (with-redefs [utils/uuid (fn [] (java.util.UUID/fromString "9088992d-d0f4-4207-9b95-c934ad071c32"))]
      (let [user {:name "Severus Snape"
                  :email "s.snape@hogwarts.edu"
                  :role "admin"
                  :password "lily"}
            _ (handler/create-user {:form-params user})
            response (handler/login {:params
                                     {:email "s.snape@hogwarts.edu" :password "lily"}})]
        (is (= 302 (:status response)))
        (is (= (str "/users/dashboard")
               (get-in response [:headers "Location"])))
        (is (= "" (:body response)))
        (is (= (java.util.UUID/fromString "9088992d-d0f4-4207-9b95-c934ad071c32")
               (get-in response [:cookies "session-id" :value]))))))

  (testing "No email exists in the database, should redirect to login page with error flash message"
    (let [user {:name "Severus Snape"
                :email "foo@gmail.com"
                :password "lily"}
          response (handler/login {:params
                                   {:email (:email user) :password (:password user)}})]

      (is (= 302 (:status response)))
      (is (= (str "/users/login")
             (get-in response [:headers "Location"])))
      (is (= {:error true
              :style-class "alert alert-danger"
              :message "User with email not found"}
             (:flash response)))
      (is (= "" (:body response)))))

  (testing "Password is wrong, should redirect to login page with error flash message"
    (let [user {:name "Severus Snape"
                :email "foo@gmail.com"
                :role "admin"
                :password "lily"}
          _ (handler/create-user {:form-params user})
          response (handler/login {:params
                                   {:email (:email user) :password "notthecorrectpassword"}})]

      (is (= 302 (:status response)))
      (is (= (str "/users/login")
             (get-in response [:headers "Location"])))
      (is (= {:error true
              :style-class "alert alert-danger"
              :message "Wrong password"}
             (:flash response)))
      (is (= "" (:body response))))))

(deftest authorization
  (testing "Should return a 302 response with flash message of \"not authorized\""
    (let [response (handler/not-authorized {})]
      (is (= 302 (:status response)))
      (is (= "/users/dashboard" (get-in response [:headers "Location"])))
      (is (= {:error true
              :style-class "alert alert-danger"
              :message "You are not authorized"}
             (:flash response)))))

  (testing "Should return a 302 response with flash message of \"not logged in\""
    (let [response (handler/not-logged-in {})]
      (is (= 302 (:status response)))
      (is (= "/users/login" (get-in response [:headers "Location"])))
      (is (= {:error true
              :style-class "alert alert-danger"
              :message "You are not logged in"}
             (:flash response))))))

(deftest login-form
  (testing "Should show the login form page if the user is not already logged-in"
    (let [response (handler/login-form {})
          html-page-login (html5 (layout/application {}
                                                     "Login"
                                                     (user-view/login-form)))]
      (is (= 200 (:status response)))
      (is (= html-page-login (:body response)))))
  (testing "Should redirect to /users/dashboard if user is logged-in"
    (let [user (user-db/create (factories/user))
          response (handler/login-form {:user (select-keys user 
                                                           [:users/id
                                                            :users/name
                                                            :users/role
                                                            :users/email])})]
      (is (= 302 (:status response)))
      (is (= "/users/dashboard"  (get-in response [:headers "Location"]))))))

(deftest signup-form
  (testing "Should show the sign up form page if the user is not already logged-in"
    (let [response (handler/signup-form{})
          html-page-login (html5 (layout/application {}
                                                     "Sign up"
                                                     (user-view/signup-form)))]
      (is (= 200 (:status response)))
      (is (= html-page-login (:body response)))))
  (testing "Should redirect to /users/dashboard if user is logged-in"
    (let [user (user-db/create (factories/user))
          response (handler/login-form {:user (select-keys user
                                                           [:users/id
                                                            :users/name
                                                            :users/role
                                                            :users/email])})]
      (is (= 302 (:status response)))
      (is (= "/users/dashboard"  (get-in response [:headers "Location"]))))))

(deftest logout
  (testing "Should logout user when the user is logged-in"
    (let [_ (user-model/create {:users/name "Severus Snape"
                                :users/role "admin"
                                :users/email "s.snape@hogwarts.edu"
                                :users/password (password/encrypt "lily")})
          user-id (:users/id (first (user-model/find-by-keys {:users/email "s.snape@hogwarts.edu"})))
          login-resp (handler/login {:params {:email "s.snape@hogwarts.edu"
                                              :password "lily"}})
          session-id-in-login-resp (str (get-in login-resp [:cookies "session-id" :value]))
          session-before-logout (first (db/query! ["SELECT * FROM SESSIONS WHERE user_id = ?" user-id]))
          logout-resp (handler/logout {:cookies {:session-id {:value session-id-in-login-resp}}})
          session-after-logout (first (db/query! ["SELECT * FROM SESSIONS WHERE user_id = ?" user-id]))]
      
      (is (= 302 (:status logout-resp)))
      (is (= "/" (get-in logout-resp [:headers "Location"])))
      (is (= session-id-in-login-resp (str (:sessions/id session-before-logout))))
      (is (nil? session-after-logout))
      (is (= nil (get-in logout-resp [:cookies "session-id" :value]))))))
