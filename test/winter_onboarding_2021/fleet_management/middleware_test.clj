(ns winter-onboarding-2021.fleet-management.middleware-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.fleet-management-service.middleware :as middleware]))

(deftest keywordizing-multipart-params
  (testing "Should keywordize multipart params"
    (is (= {:multipart-params {:foo "bar"
                               :hello "world"}}
           ((middleware/keywordize-multipart-params identity)
            {:multipart-params {"foo" "bar"
                                "hello" "world"}})))))

(deftest req-log-msg
  (testing "Should return a log message containing the request keys of :body :params :uri :headers :request-method"
    (is (= "Request {:body \"Hello World\"}"
           (middleware/make-req-log-msg {:body "Hello World"})))

    (is (= "Request {}"
           (middleware/make-req-log-msg {:sensitive-key "sensitive value, Please dont log me"})))))

(deftest response-log-msg
  (testing "Should return a log message containing the response map"
    (is (= "Response {:status 200, :body \"Hello World\"}"
           (middleware/make-response-log-msg {:status 200
                                              :body "Hello World"})))))
