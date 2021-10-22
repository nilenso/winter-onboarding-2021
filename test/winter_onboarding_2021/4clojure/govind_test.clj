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

(deftest nth-element
  (testing "Returns the nth element"
    (is (= (solutions/problem21 '(4 5 6 7) 2) 6))
    (is (= (solutions/problem21 [:a :b :c] 0) :a))
    (is (= (solutions/problem21 [1 2 3 4] 1) 2))
    (is (= (solutions/problem21 '([1 2] [3 4] [5 6]) 2) [5 6]))))

(deftest interleave-test
  (testing "Interleaves two lists"
    (is (= (solutions/problem39 [1 2 3] [:a :b :c]) '(1 :a 2 :b 3 :c)))
    (is (= (solutions/problem39 [1 2] [3 4 5 6]) '(1 3 2 4)))
    (is (= (solutions/problem39 [1 2 3 4] [5]) [1 5]))
    (is (= (solutions/problem39 [30 20] [25 15]) [30 25 20 15]))))

(deftest reverse-interleave-test
  (testing "Reverses the interleave process"
    (is (= (solutions/problem43 [1 2 3 4 5 6] 2) '((1 3 5) (2 4 6))))
    (is (= (solutions/problem43 (range 9) 3) '((0 3 6) (1 4 7) (2 5 8))))
    (is (= (solutions/problem43 (range 10) 5) '((0 5) (1 6) (2 7) (3 8) (4 9))))))

(deftest rotate-list
  (testing "Rotates a list"
    (is (= (solutions/problem44 2 [1 2 3 4 5]) '(3 4 5 1 2)))
    (is (= (solutions/problem44 -2 [1 2 3 4 5]) '(4 5 1 2 3)))
    (is (= (solutions/problem44 6 [1 2 3 4 5]) '(2 3 4 5 1)))
    (is (= (solutions/problem44 1 '(:a :b :c)) '(:b :c :a)))
    (is (= (solutions/problem44 -4 '(:a :b :c)) '(:c :a :b)))))

(deftest longest-subseq
  (testing "Returns the longest sub sequence"
    (is (= (solutions/problem53 [1 0 1 2 3 0 4 5]) [0 1 2 3]))
    (is (= (solutions/problem53 [5 6 1 3 2 7]) [5 6]))
    (is (= (solutions/problem53 [2 3 3 4 5]) [3 4 5]))
    (is (= (solutions/problem53 [7 6 5 4]) []))))