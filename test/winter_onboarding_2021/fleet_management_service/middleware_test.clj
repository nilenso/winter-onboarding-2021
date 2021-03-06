(ns winter-onboarding-2021.fleet-management-service.middleware-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.middleware :as middleware]
            [winter-onboarding-2021.fleet-management-service.handlers.user :as handler]
            [clojure.data.json :as json]
            [clj-http.client :as client]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest keywordizing-multipart-params
  (testing "Should keywordize multipart params"
    (is (= {:multipart-params {:foo "bar"
                               :hello "world"}}
           ((middleware/keywordize-multipart-params identity)
            {:multipart-params {"foo" "bar"
                                "hello" "world"}})))))

(deftest returns-handler-which-appends-user-to-request
  (with-redefs [client/post (constantly {:body (json/json-str {"success" true})})]
    (testing "Should add hashmap containing user data to request"
      (let [handler identity
            user {:name "Severus Snape"
                  :email "s.snape@hogwarts.edu"
                  :role "admin"
                  :password "lily"
                  :g-recaptcha-response "abcd123"}
            _ (handler/create-user {:form-params user})
            login-response (handler/login {:params {:email "s.snape@hogwarts.edu"
                                                    :password "lily"}})
            session-id (str (get-in login-response [:cookies "session-id" :value]))
            request {:cookies {:session-id {:value session-id}}}]
        (is (= #:users{:name "Severus Snape"
                       :email "s.snape@hogwarts.edu"}
               (select-keys (:user ((middleware/append-user-to-request handler)
                                    request))
                            [:users/name :users/email])))))))
