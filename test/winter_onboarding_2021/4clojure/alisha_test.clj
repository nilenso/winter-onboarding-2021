(ns winter-onboarding-2021.4clojure.alisha-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.4clojure.alisha :as solutions]))

(deftest function-list-equal-to-quote-list
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
  (testing "to check keys added to set using conj function"
    (is (= (solutions/problem9) #{1 2 3 4}))))

(deftest hashmap-lookup
  (testing "to check keys added to set using conj function")
  (is (=  (solutions/problem10) ((hash-map :a 10, :b 20, :c 30) :b)))
  (is (=  (solutions/problem10) (:b {:a 11, :b 20, :c 30}))))

(deftest conj-on-maps
  (testing "to check keys added to map using conj function"
    (is (= {:a 1, :b 2, :c 3} (conj {:a 1} (solutions/problem11) [:c 3])))))

(deftest sequences
  (testing "operation on sequences with functions first second last"
    (is (= (solutions/problem12) (first '(3 2 1))))
    (is (= (solutions/problem12) (second [2 3 4])))
    (is (= (solutions/problem12) (last (list 1 2 3))))))

(deftest rest-function
  (testing "to check return of rest function"
    (is (= (solutions/problem13) (rest [10 20 30 40])))))

(deftest function-declarations
  (testing "to check diff ways to declare functions"
    (is (= (solutions/problem14) ((fn add-five [x] (+ x 5)) 3)))
    (is (= (solutions/problem14) ((fn [x] (+ x 5)) 3)))
    (is (= (solutions/problem14) (#(+ % 5) 3)))
    (is (= (solutions/problem14) ((partial + 5) 3)))))

(deftest double-func
  (testing "function to double the number"
    (is (= ((solutions/problem15) 2) 4))
    (is (= ((solutions/problem15) 3) 6))
    (is (= ((solutions/problem15) 11) 22))
    (is (= ((solutions/problem15) 7) 14))))

(deftest personalised-greeting
  (testing "personalised greeting"
    (is (= ((solutions/problem16) "Dave") "Hello, Dave!"))
    (is (= ((solutions/problem16) "Jenn") "Hello, Jenn!"))
    (is (= ((solutions/problem16) "Rhea") "Hello, Rhea!"))))

(deftest map-function
  (testing "To understand map function"
    (is (= (solutions/problem17) (map #(+ % 5) '(1 2 3))))))

(deftest filter-function
  (testing "Understand filter function"
    (is (= (solutions/problem18) (filter #(> % 5) '(3 4 5 6 7))))))

(deftest last-element-in-sequence
  (testing "Function to return last element in a sequence"
    (is (= ((solutions/problem19) [1 2 3 4 5]) 5))
    (is (= ((solutions/problem19) '(5 4 3)) 3))
    (is (= ((solutions/problem19) ["b" "c" "d"]) "d"))))

(deftest penultimate-element
  (testing "Function to return second last element in a sequence"
    (is (= ((solutions/problem20) (list 1 2 3 4 5)) 4))
    (is (= ((solutions/problem20) ["a" "b" "c"]) "b"))
    (is (= ((solutions/problem20) [[1 2] [3 4]]) [1 2]))))

(deftest count-function
  (testing "To operate on count function"
    (is (= ((solutions/problem22) '(1 2 3 3 1)) 5))
    (is (= ((solutions/problem22) "Hello World") 11))
    (is (= ((solutions/problem22) [[1 2] [3 4] [5 6]]) 3))
    (is (= ((solutions/problem22) '(13)) 1))
    (is (= ((solutions/problem22) '(:a :b :c)) 3))))

(deftest sequence-reverse
  (testing "To reverse a sequence"
    (is (= (solutions/problem23 [1 2 3 4 5]) [5 4 3 2 1]))
    (is (= (solutions/problem23 (sorted-set 5 7 2 7)) '(7 5 2)))
    (is (= (solutions/problem23 [[1 2] [3 4] [5 6]]) [[5 6] [3 4] [1 2]]))))