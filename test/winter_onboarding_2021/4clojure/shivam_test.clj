(ns winter-onboarding-2021.4clojure.shivam-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.4clojure.shivam :as solutions]))


(deftest list-conj
  (testing "Conjuction of & to lists")
  (is (= (solutions/problem5)  (conj '(2 3 4) 1)))
  (is (= (solutions/problem5)  (conj '(3 4) 2 1))))

(deftest vector-construction
  (testing "Construction of vectors")
  (is (= (solutions/problem6) (list :a :b :c) (vec '(:a :b :c)) (vector :a :b :c))))

(deftest vector-conj
  (testing "Conjuction of & to vectors")
  (is (= (solutions/problem7)  (conj [1 2 3] 4)))
  (is (= (solutions/problem7)  (conj [1 2 3] 4))))

(deftest sets-contruction
  (testing "Construction of & to sets")
  (is (= (solutions/problem8)  (set '(:a :a :b :c :c :c :c :d :d))))
  (is (= (solutions/problem8)  (clojure.set/union #{:a :b :c} #{:b :c :d}))))

(deftest set-conjuction
  (testing "Conjuction of vectors")
  (is (= #{1 2 3 4} (conj #{1 4 3} (solutions/problem9)))))

(deftest lookup-map
  (testing "Looking up a specific value in a hash-map")
  (is (= (solutions/problem10) ((hash-map :a 10, :b 20, :c 30) :b))))

(deftest map-conjuction
  (testing "Conjuction of maps and/or keys")
  (is (= {:a 1, :b 2, :c 3} (conj {:a 1} (solutions/problem11) [:c 3]))))

(deftest seq-colls
  (testing "Sequencing operations on collections")
  (is (= (solutions/problem12) (first '(3 2 1))))
  (is (= (solutions/problem12) (second [2 3 4])))
  (is (= (solutions/problem12) (last (list 1 2 3)))))

(deftest rest-func
  (testing "Rest function on a collection")
  (is (= (solutions/problem13) (rest [10 20 30 40]))))

(deftest creating-fns
  (testing "Vaious ways of creating functions")
  (is (= (solutions/problem14) ((fn add-five [x] (+ x 5)) 3)))
  (is (= (solutions/problem14) ((fn [x] (+ x 5)) 3)))
  (is (= (solutions/problem14) (#(+ % 5) 3)))
  (is (= (solutions/problem14) ((partial + 5) 3))))

(deftest double-n
  (testing "Doubling a number")
  (is (= (solutions/problem15 2) 4))
  (is (= (solutions/problem15 3) 6))
  (is (= (solutions/problem15 11) 22))
  (is (= (solutions/problem15 7) 14)))

(deftest greeting
  (testing "Joining two strings to form a personalised greeting")
  (is (= (solutions/problem16 "Dave") "Hello, Dave!"))
  (is (= (solutions/problem16 "Jenn") "Hello, Jenn!"))
  (is (= (solutions/problem16 "Rhea") "Hello, Rhea!")))

(deftest add-five
  (testing "Demonstrating the use of map in applying a function on a collection")
  (is (= (solutions/problem17) (map #(+ % 5) '(1 2 3)))))

(deftest filter-func
  (testing "Demonstrating the use of map in applying a function on a collection")
  (is (= (solutions/problem18) (filter #(> % 5) '(3 4 5 6 7)))))

(deftest get-last
  (testing "Getting the last element")
  (is (= ((solutions/problem19) [1 2 3 4 5]) 5))
  (is (= ((solutions/problem19) '(5 4 3)) 3))
  (is (= ((solutions/problem19) ["b" "c" "d"]) "d")))

(deftest get-penultimate
  (testing "Getting the penultimate(last 2nd) element from a collection")
  (is (= (solutions/problem20 (list 1 2 3 4 5)) 4))
  (is (= (solutions/problem20 ["a" "b" "c"]) "b"))
  (is (= (solutions/problem20 [[1 2] [3 4]]) [1 2])))