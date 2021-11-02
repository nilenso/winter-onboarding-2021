(ns winter-onboarding-2021.dice-roller.alisha-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.alisha.core :as dnd-dice]))

(def sample-outcomes '({:type :dice-roll-outcome :num-faces 10 :value 6}
                      {:type :dice-roll-outcome :num-faces 10 :value 1}
                      {:type :dice-roll-outcome :num-faces 10 :value 2}
                      {:type :dice-roll-outcome :num-faces 10 :value 8}
                      {:type :dice-roll-outcome :num-faces 10 :value 5}
                      {:type :dice-roll-outcome :num-faces 10 :value 3}))

(def small-sample-outcomes '({:type :dice-roll-outcome :num-faces 4 :value 1}
                             {:type :dice-roll-outcome :num-faces 4 :value 1}
                             {:type :dice-roll-outcome :num-faces 4 :value 4}))

(deftest dice-notation
  (testing "to check dice-notation return a hashmap with rolls and faces"
    (is (= (dnd-dice/dice-notation 3 4) {:num-rolls 3 :num-faces 4}))))

(deftest set-operation
  (testing "to check set-operation return a hashmap with operator and selector"
    (is (= (dnd-dice/set-operation "k" ">") {:op "k" :selector ">"}))
    (is (= (dnd-dice/set-operation "l" "") {:op "l" :selector ""}))
    (is (= (dnd-dice/set-operation "rr" "h") {:op "rr" :selector "h"}))
    (is (= (dnd-dice/set-operation "k" "<") {:op "k" :selector "<"}))))

(deftest set-expression
  (testing "to check set-expression return a hashmap with dice-notation and set-operation"
    (is (= (dnd-dice/set-expression {:num-rolls 3 :num-faces 4}
                                    {:op "k" :selector ">"})
           {:dice-notation {:num-rolls 3 :num-faces 4}
            :set-operation {:op "k" :selector ">"}}))))

(deftest generate-dice-roll-outcome
  (testing "to check for random values getting generated when dice is roll"
    (with-redefs [rand-int (fn [x] (- x 1))] ;;rand int would return only the highest dice value always
      (is (= (dnd-dice/perform-dice-roll {:num-rolls 3
                                          :num-faces 4})
             '({:type :dice-roll-outcome :num-faces 4 :value 4}
              {:type :dice-roll-outcome :num-faces 4 :value 4}
              {:type :dice-roll-outcome :num-faces 4 :value 4}))))))

(deftest get-x-highest
  (testing "to get first x highest values"
    (is (= (dnd-dice/get-x-highest sample-outcomes 3) 
           '({:type :dice-roll-outcome :num-faces 10 :value 8}
            {:type :dice-roll-outcome :num-faces 10 :value 6}
            {:type :dice-roll-outcome :num-faces 10 :value 5})))))

(deftest get-x-lowest
  (testing "to get first x lowest values"
    (is (= (dnd-dice/get-x-lowest sample-outcomes 3) 
           '({:type :dice-roll-outcome :num-faces 10 :value 1}
            {:type :dice-roll-outcome :num-faces 10 :value 2}
            {:type :dice-roll-outcome :num-faces 10 :value 3})))))

(deftest get-greater-than-x
  (testing "to get values greater than x"
    (is (= (dnd-dice/get-greater-than-x sample-outcomes 3) 
           '({:type :dice-roll-outcome :num-faces 10 :value 6}
            {:type :dice-roll-outcome :num-faces 10 :value 8}
            {:type :dice-roll-outcome :num-faces 10 :value 5})))))

(deftest get-lesser-than-x
  (testing "to get values lesser than x"
    (is (= (dnd-dice/get-lesser-than-x sample-outcomes 3) 
           '({:type :dice-roll-outcome :num-faces 10 :value 1}
            {:type :dice-roll-outcome :num-faces 10 :value 2})))))

(deftest get-equal-with-x
  (testing "to get all the values equal to x"
    (is (= (dnd-dice/get-equal-with-x sample-outcomes 3) 
           '({:type :dice-roll-outcome :num-faces 10 :value 3})))))

(deftest keep-from-dice-outcomes
  (testing "to keep all the value returned after operating on selector-operation"
    (is (= (dnd-dice/keep-from-dice-outcomes dnd-dice/get-x-highest sample-outcomes 2) 
           '({:type :dice-roll-outcome :num-faces 10 :value 8}
            {:type :dice-roll-outcome :num-faces 10 :value 6})))))

(deftest remove-once
  (testing "to remove an element from a sequence only once which matches the predicate"
    (let [pred #(= %1 {:type :dice-roll-outcome :num-faces 10 :value 1})] ;;checks if value of sample-outcome is 1
      (is (= (dnd-dice/remove-once pred sample-outcomes) 
             '({:type :dice-roll-outcome :num-faces 10 :value 6}
              {:type :dice-roll-outcome :num-faces 10 :value 2}
              {:type :dice-roll-outcome :num-faces 10 :value 8}
              {:type :dice-roll-outcome :num-faces 10 :value 5}
              {:type :dice-roll-outcome :num-faces 10 :value 3}))))))

(deftest drop-from-dice-outcomes
  (testing "to drop a subset of outcomes returned from executing selector operation from the dice-outcomes"
       (is (= (dnd-dice/drop-from-dice-outcomes dnd-dice/get-x-highest
                                                small-sample-outcomes
                                                1)
              '({:type :dice-roll-outcome :num-faces 4 :value 1}
                {:type :dice-roll-outcome :num-faces 4 :value 1})))))

#_(deftest reroll-dice
  (testing "to re-roll dice until none match"
    (with-redefs [rand-int (fn [x] (- x 1))]
      (let [selector dnd-dice/get-lesser-than-x
            initial-outcomes [1 2 3 4 4 2]
            x 2
            dice-notation {:num-rolls 6 :num-faces 5}]
        (is (= (dnd-dice/reroll-dice selector
                                     initial-outcomes
                                     x
                                     dice-notation)
             [5 5 5 5 5 5])))))
  (testing "return error if re-roll is greater than maximum re-rolls"
    (with-redefs [rand-int (fn [x] (- x 1))]
      (let [selector dnd-dice/get-greater-than-x
            initial-outcomes [1 2 3 4 4 2]
            x 1
            dice-notation {:num-rolls 6 :num-faces 5}]
        (is (= (dnd-dice/reroll-dice selector
                                     initial-outcomes
                                     x
                                     dice-notation)
               [5 5 5 5 5 5]))))))

;;[1 2 3 4 4 2], x = 1
;;all the outcomes will be greater than 1
;;reroll until 4 are not in set
