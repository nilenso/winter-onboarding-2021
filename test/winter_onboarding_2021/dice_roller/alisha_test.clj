(ns winter-onboarding-2021.dice-roller.alisha-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.alisha.core :as dnd-dice]))

;; (deftest dice-roll-outcomes
;;   (testing "to check dice-roll-set return a hashmap with values"
;;     (is (= (dnd-dice/dice-roll-outcomes [1 2 3 4]) [1 2 3 4]))))

(deftest dice-notation 
  (testing "to check dice-notation return a hashmap with rolls and faces"
    (is (= (dnd-dice/dice-notation 3 4) {:rolls 3 :faces 4 }))))

(deftest set-operation
  (testing "to check set-operation return a hashmap with operator and selector"
    (is (= (dnd-dice/set-operation "k" ">") {:op "k" :selector ">"}))
    (is (= (dnd-dice/set-operation "l" "") {:op "l" :selector ""}))
    (is (= (dnd-dice/set-operation "rr" "h") {:op "rr" :selector "h"}))
    (is (= (dnd-dice/set-operation "k" "<") {:op "k" :selector "<"}))))

(deftest set-expression
  (testing "to check set-expression return a hashmap with dice-notation and set-operation"
    (is (= (dnd-dice/set-expression {:rolls 3 :faces 4} 
                                   {:op "k" :selector ">"})
           {:dice-notation {:rolls 3 :faces 4}
            :set-operation {:op "k" :selector ">"}}))))

(deftest perform-dice-roll
  (testing "to check for random values getting geneerated when dice is roll"
    (with-redefs [rand-int (fn [x] (- x 1))] ;;rand int would return only the highest dice value always
      (is (= (dnd-dice/perform-dice-roll {:rolls 3 :faces 4}) [4 4 4])))))

(deftest get-first-x-highest
  (testing "to get first x highest values"
    (is (= (dnd-dice/get-first-x-highest [1 2 3 4] 3) [4 3 2]))))

(deftest get-first-x-lowest
  (testing "to get first x lowest values"
    (is (= (dnd-dice/get-first-x-lowest [1 6 3 4] 3) [1 3 4]))))

(deftest get-greater-than-x
  (testing "to get values greater than x"
    (is (= (dnd-dice/get-greater-than-x [1 2 3 4] 3) [4]))))

(deftest get-lesser-than-x
  (testing "to get values lesser than x"
    (is (= (dnd-dice/get-lesser-than-x [1 2 3 4] 3) [1 2]))))

(deftest get-equal-than-x
  (testing "to get all the values equal to x"
    (is (= (dnd-dice/get-equal-with-x [1 2 2 3 4 5] 2) [2 2]))))

(deftest keep-from-dice-outcomes
  (testing "to keep all the value returned after operating on selector-operation"
    (is (= (dnd-dice/keep-from-dice-outcomes #(dnd-dice/get-first-x-highest %1 %2) [1 2 3 4 5] 2) [5 4]))))

(deftest remove-once 
  (testing "to remove an element from a sequence only once which matches the predicate"
    (let [pred #(= %1 1)] ;;checks if value is 1
      (is (= (dnd-dice/remove-once pred [1 2 3 4 1]) [2 3 4 1])))))

(deftest drop-from-dice-outcomes
  (testing "to drop a subset of outcomes returned from executing selector operation from the dice-outcomes"
    (is (= (dnd-dice/drop-from-dice-outcomes #(dnd-dice/get-first-x-highest %1 %2) [1 2 2 3 1 3] 3) [1 2 1]))))

(deftest reroll-dice
  (testing "to re-roll dice until none match"
    (with-redefs [rand-int (fn [x] (- x 1))]
     (is (= (dnd-dice/reroll-dice #(dnd-dice/get-lesser-than-x %1 %2) [1 2 3 4 4 2] 2 {:rolls 6 :faces 5}) [5 5 5 5 5 5])))))
