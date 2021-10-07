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

