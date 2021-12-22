(ns winter-onboarding-2021.fleet-management-service.views.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [hiccup-find.core :as hf]
            [winter-onboarding-2021.fleet-management-service.views.core :as core]))


(deftest index-view
  (testing "Should return hello string"
    (is (= "Hello Hiccup & Bootstrap"
           (hf/hiccup-text (core/index))))))
