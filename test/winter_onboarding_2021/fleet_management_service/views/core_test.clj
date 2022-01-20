(ns winter-onboarding-2021.fleet-management-service.views.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [hiccup-find.core :as hf]
            [winter-onboarding-2021.fleet-management-service.views.core :as core]))

(deftest index-view
  (testing "Should return heading of landing page i.e Welcome to fms"
    (is (= "Welcome to fms"
           (hf/hiccup-text (hf/hiccup-find [:h1] (core/index)))))))
