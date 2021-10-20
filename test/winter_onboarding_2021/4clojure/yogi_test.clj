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

(deftest check_map
  (testing "map")
  (is (= (solutions/prob10) ((hash-map :a 10, :b 20, :c 30) :b))))

(deftest check_conj_map
  (testing "conj on map")
  (is (= (solutions/prob11) {:a 1, :b 2, :c 3})))


(deftest check_map_index
  (testing "conj on map")
  (is (= (solutions/prob12)
         (second [2 3 4]))))

(deftest check_rest_on_map
  (testing "rest on map")
  (is (= (solutions/prob13) (rest [10 20 30 40]))))

(deftest prob14actual
  (testing "functions")
  (is (= (solutions/prob14actual) ((partial + 5) 3))))

(deftest prob15
  (testing "double function")
  (is (= (solutions/prob15 6) 12)))

(deftest prob16
  (testing "String append")
  (is (= (solutions/prob16 "Rhea") "Hello, Rhea!")))

(deftest prob17
  (testing "map function")
  (is (= (solutions/prob17) (map #(+ % 5) '(1 2 3)))))

(deftest prob18
  (testing "filters")
  (is (= (solutions/prob18) (filter #(> % 5) '(3 4 5 6 7)))))

(deftest prob19
  (testing "last without using 'last'")
  (is (= (solutions/prob19) (first (reverse [1 2 3 4 5])))))

(deftest prob20
  (testing "second last")
  (is (= (solutions/prob20) (second (reverse [1 2 3 4 5])))))




(deftest prob21
  (testing "nth")
  (is 
   (= (solutions/prob21 '([1 2] [3 4] [5 6]) 2)
      [5 6]))

  (is  
   (= (solutions/prob21 '(4 5 6 7) 2)
      6))

  (is  
   (= (solutions/prob21 [:a :b :c] 0)
      :a))

  (is  
   (= (solutions/prob21 [1 2 3 4] 1)
      2))
  
  (is  
   (= (solutions/prob21 '([1 2] [3 4] [5 6]) 2)
      [5 6]))) 





(deftest prob22
  (testing "count")
  (is 
   (= (solutions/prob22 '(1 2 3 3 1)) 
      5))
  (is   
   (= (solutions/prob22 "Hello World")
      11))
  (is  
   (= (solutions/prob22 [[1 2] [3 4] [5 6]])
      3))
  (is
   (= (solutions/prob22 '(13))
      1))
  (is   
   (= (solutions/prob22 '(:a :b :c))
      3)))


(deftest prob23
  (testing "reverse")
  (is
   (= (solutions/prob23 [1 2 3 4 5])
      [5 4 3 2 1]))

  (is
   (= (solutions/prob23 (sorted-set 5 7 2 7))
      '(7 5 2)))

  (is 
   (= ( solutions/prob23 [[1 2] [3 4] [5 6]]) 
      [[5 6] [3 4] [1 2]])))


