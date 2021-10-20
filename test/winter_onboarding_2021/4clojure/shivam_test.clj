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
  (is (= (solutions/problem7) (conj [1 2 3] 4)))
  (is (= (solutions/problem7) (conj [1 2 3] 4))))

(deftest sets-contruction
  (testing "Construction of & to sets")
  (is (= (solutions/problem8) (set '(:a :a :b :c :c :c :c :d :d))))
  (is (= (solutions/problem8) (clojure.set/union #{:a :b :c} #{:b :c :d}))))

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

(deftest get-nth-element
  (testing "Getting the nth element element from a collection")
  (is (= (solutions/problem21 '(4 5 6 7) 2) 6))
  (is (= (solutions/problem21 [:a :b :c] 0) :a))
  (is (= (solutions/problem21 [1 2 3 4] 1) 2))
  (is (= (solutions/problem21 '([1 2] [3 4] [5 6]) 2) [5 6])))

(deftest get-len-seq
  (testing "Getting the length of the sequence")
  (is (= (solutions/problem22 '(1 2 3 3 1)) 5))
  (is (= (solutions/problem22 "Hello World") 11))
  (is (= (solutions/problem22 [[1 2] [3 4] [5 6]]) 3))
  (is (= (solutions/problem22 '(13)) 1))
  (is (= (solutions/problem22 '(:a :b :c)) 3)))

(deftest reverse-seq
  (testing "a function which reverses a sequence")
  (is (= (solutions/problem23 [1 2 3 4 5]) [5 4 3 2 1]))
  (is (= (solutions/problem23 (sorted-set 5 7 2 7)) '(7 5 2)))
  (is (= (solutions/problem23 [[1 2] [3 4] [5 6]]) [[5 6] [3 4] [1 2]])))

(deftest sum-seq
  (testing "a function which returns the sum of a sequence of numbers")
  (is (= (solutions/problem24 [1 2 3]) 6))
  (is (= (solutions/problem24 (list 0 -2 5 5)) 8))
  (is (= (solutions/problem24 #{4 2 1}) 7))
  (is (= (solutions/problem24 '(0 0 -1)) -1))
  (is (= (solutions/problem24 '(1 10 3)) 14)))


(deftest only-odd
  (testing "a function which returns only the odd numbers from a sequence")
  (is (= (solutions/problem25 #{1 2 3 4 5}) '(1 3 5)))
  (is (= (solutions/problem25 [4 2 1 6]) '(1)))
  (is (= (solutions/problem25 [2 2 4 6]) '()))
  (is (= (solutions/problem25 [1 1 1 3]) '(1 1 1 3))))

(deftest fibonacci
  (testing "a function which returns the first X fibonacci numbers")
  (is (= (solutions/problem26 3) '(0 1 1)))
  (is (= (solutions/problem26 6) '(0 1 1 2 3 5)))
  (is (= (solutions/problem26 8) '(0 1 1 2 3 5 8 13))))

(deftest palindrome
  (testing "a function which returns the sum of a sequence of numbers")
  (is (false? (solutions/problem27 '(1 2 3 4 5))))
  (is (true? (solutions/problem27 "racecar")))
  (is (true? (solutions/problem27 [:foo :bar :foo])))
  (is (true? (solutions/problem27 '(1 1 3 3 1 1))))
  (is (false? (solutions/problem27 '(:a :b :c)))))

(deftest my-flatten
  (testing "a function which flattens a sequence")
  (is (= (solutions/problem28 '((1 2) 3 [4 [5 6]])) '(1 2 3 4 5 6)))
  (is (= (solutions/problem28 ["a" ["b"] "c"]) '("a" "b" "c")))
  (is (= (solutions/problem28 '((((:a))))) '(:a))))

(deftest find-capital-letters
  (testing "a function which takes a string and returns a new string containing only the capital letters.")
  (is (= (solutions/problem29 "HeLlO, WoRlD!") "HLOWRD"))
  (is (empty? (solutions/problem29 "nothing")))
  (is (= (solutions/problem29 "$#A(*&987Zf") "AZ")))

(deftest remove-consecutive-dups
  (testing "a function which removes consecutive duplicates from a sequence")
  (is (= (apply str (solutions/problem30 "Leeeeeerrroyyy")) "Leroy"))
  (is (= (solutions/problem30 [1 1 2 3 3 2 2 3]) '(1 2 3 2 3)))
  (is (= (solutions/problem30 [[1 2] [1 2] [3 4] [1 2]]) '([1 2] [3 4] [1 2]))))


(deftest duplicate-elements
  (testing "duplicates")
  (is (= (solutions/problem32 [1 2 3]) '(1 1 2 2 3 3)))
  (is (= (solutions/problem32 [:a :a :b :b]) '(:a :a :a :a :b :b :b :b)))
  (is (= (solutions/problem32 [[1 2] [3 4]]) '([1 2] [1 2] [3 4] [3 4])))
  (is (= (solutions/problem32 [44 33]) [44 44 33 33])))

;; (deftest continuous-word-chain
;;   (testing "the continuity of the word chain")
;;   (is (= true (solutions/problem82? #{"hat" "coat" "dog" "cat" "oat" "cot" "hot" "hog"})))
;;   (is (= false (solutions/problem82? #{"cot" "hot" "bat" "fat"})))
;;   (is (= false (solutions/problem82? #{"to" "top" "stop" "tops" "toss"})))
;;   (is (= true (solutions/problem82? #{"spout" "do" "pot" "pout" "spot" "dot"})))
;;   (is (= true (solutions/problem82? #{"share" "hares" "shares" "hare" "are"})))
;;   (is (= false (solutions/problem82? #{"share" "hares" "hare" "are"}))))

(deftest filter-perfect-squares
  (testing "a function which returns a new comma separated string that only contains the numbers which are perfect squares")
  (is (= (solutions/problem74 "4,5,6,7,8,9") "4,9"))
  (is (= (solutions/problem74 "15,16,25,36,37") "16,25,36")))

(deftest kebab-to-camelcase
  (testing "a function which takes lower-case hyphen-separated strings and converts them to camel-case strings")
  (is (= (solutions/problem102 "something") "something"))
  (is (= (solutions/problem102 "multi-word-key") "multiWordKey"))
  (is (= (solutions/problem102 "leaveMeAlone") "leaveMeAlone")))
