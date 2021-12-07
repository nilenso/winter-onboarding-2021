(ns winter-onboarding-2021.fleet-management.config-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.fleet-management-service.config :as config]))

(deftest profile
  (testing "Should return :test profile since tests should always be run in Test Environment"
    (with-redefs [config/env (constantly "test")]
      (is (= :test (config/profile))))))
