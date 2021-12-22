(ns winter-onboarding-2021.fleet-management-service.middleware-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.fleet-management-service.middleware :as middleware]))

(deftest keywordizing-multipart-params
  (testing "Should keywordize multipart params"
    (is (= {:multipart-params {:foo "bar"
                               :hello "world"}}
           ((middleware/keywordize-multipart-params identity)
            {:multipart-params {"foo" "bar"
                                "hello" "world"}})))))
