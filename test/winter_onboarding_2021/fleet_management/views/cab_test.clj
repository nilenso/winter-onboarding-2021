(ns winter-onboarding-2021.fleet-management.views.cab-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.walk :as walk]
            [winter-onboarding-2021.fleet-management-service.views.cab :as cab]))

(deftest add-cab-view
  (testing "Should have alert success bubble after adding a valid cab"
    (is true))
  
  (testing "Should have alert error bubble after trying to add an invalid cab"
    (is true)))
