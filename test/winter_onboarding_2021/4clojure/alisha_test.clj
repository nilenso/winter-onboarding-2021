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

(deftest set-contain-unique-values
  (testing "to check set contain unique values"
    (is (= (solutions/problem8) #{:a :b :c :d}))))

(deftest conj-on-sets
  (testing "to check keys added to set using conj function")
  (is (= (solutions/problem9) #{1 2 3 4})))

(deftest hashmap-lookup
  (testing "to check keys added to set using conj function")
  (is (= (solutions/problem10) (hash-map :a 10, :b 20, :c 30) :b))
  ((is (= (solutions/problem10) (:b {:a 10, :b 20, :c 30})))))

(deftest conj-on-maps
  (testing "to check keys added to map using conj function")
  (is (= {:a 1, :b 2, :c 3} (conj {:a 1} (solutions/problem11) [:c 3]))))

(deftest sequences
  (testing "operation on sequences with functions first second last")
  (is (= (solutions/problem12) (first '(3 2 1))))
  (is (= (solutions/problem12) (second [2 3 4])))
  (is (= (solutions/problem12) (last (list 1 2 3)))))

(deftest rest-function
  (testing "to check return of rest function"
    (is (= (solutions/problem13) (rest [10 20 30 40])))))

(deftest function-declarations
  (testing "to check diff ways to declare functions")
  (is (= (solutions/problem14) ((fn add-five [x] (+ x 5)) 3)))
  (is (= (solutions/problem14) ((fn [x] (+ x 5)) 3)))
  (is (= (solutions/problem14) (#(+ % 5) 3)))
  (is (= (solutions/problem14) ((partial + 5) 3))))

(deftest double-func
  (testing "function to double the number")
  (is (= ((solutions/problem15) 2) 4))
  (is (= ((solutions/problem15) 3) 6))
  (is (= ((solutions/problem15) 11) 22))
  (is (= ((solutions/problem15) 7) 14)))



