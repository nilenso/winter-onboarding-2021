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
    (with-redefs [rand-int (fn [x] x)]
      (is (= (dnd-dice/perform-dice-roll {:rolls 3 :faces 4}) [5 5 5])))))

