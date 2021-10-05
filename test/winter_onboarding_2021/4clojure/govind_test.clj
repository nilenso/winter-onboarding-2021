(ns winter-onboarding-2021.4clojure.govind-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.4clojure.govind :as solutions]))

(deftest is-equal-to-true
  (testing "Returns true"
   (is (= (solutions/problem1) true))))

(deftest simple-math
  (testing "Evaluates a simple math expression"
    (is (= (- 10 (* 2 3)) (solutions/problem2)))))

(deftest string-uppercase
  (testing "Converts as string to uppercase"
    (is (= (solutions/problem3) (.toUpperCase "hello world")))))