(ns winter-onboarding-2021.4clojure.yogi-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.4clojure.yogi :as solutions]))


(deftest just_checking_problem4
  (testing "Should return true")
  (is (= (solutions/problem4) true)))

(deftest checking_if_returns_list
  (testing "Should return list")
  (is (= (solutions/prob14) '(:a :b :c))))

(deftest checking_conj
  (testing "should_return_appened list")
  (is (= (solutions/prob5) (conj '(2 3 4) 1))))


(deftest compare_list_vector
  (testing "comparing list with vector")
  (is (= (solutions/prob6) (list :a :b :c) (vec '(:a :b :c)) (vector :a :b :c))))


(deftest checking_vector
  (testing "vector conj")
  (is (= (solutions/prob7) (conj [1 2 3] 4))))

(deftest check_set
  (testing "check set")
  (is (= (solutions/prob8) (set '(:a :a :b :c :c :c :c :d :d)))))

(deftest check_set_conj
  (testing "set conj")
  (is (= (solutions/prob9) #{1 2 3 4})))
