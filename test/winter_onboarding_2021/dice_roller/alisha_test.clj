(ns winter-onboarding-2021.dice-roller.alisha-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.alisha.core :as dnd-dice]))

(deftest dice-roll-set
  (testing "to check dice-roll-set return a hashmap with values"
    (is (= (dnd-dice/dice-roll-set [1 2 3 4]) {:type "DiceRollSet" :values [1 2 3 4]}))))

(deftest dice-notation 
  (testing "to check dice-notation return a hashmap with rolls and faces"
    (is (= (dnd-dice/dice-notation 3 4) {:type "DiceNotation" :rolls 3 :faces 4 }))))
