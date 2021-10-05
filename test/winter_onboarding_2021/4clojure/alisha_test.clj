(ns winter-onboarding-2021.4clojure.alisha-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.4clojure.alisha :as solutions]))

(deftest is-function-list-equal-to-quote-list
  (testing "Checks eqality of lists constructed from from function with quoted form"
    (is (= (solutions/problem4) '(:a :b :c)))))

(deftest conj-function
  (testing "conj function will return a new list with one or more items added to the front"
    (is (= (solutions/problem5) '(1 2 3 4)))))

(deftest vector-list-comparison
  (testing "comparison of vector & list"
    (is (= (solutions/problem6) [:a :b :c]))))

(deftest conj-function-for-vector
  (testing "to check conj function appends to the end of vector"
    (is (= (solutions/problem7) [1 2 3 4]))))

