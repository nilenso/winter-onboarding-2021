(ns winter-onboarding-2021.dice-roller.alisha-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.alisha.core :as dnd-dice]
            [winter-onboarding-2021.dice-roller.alisha.data_struct :as data-struct]))

(def sample-rolls '({:type :dice-roll-outcome :num-faces 10 :value 6}
                      {:type :dice-roll-outcome :num-faces 10 :value 1}
                      {:type :dice-roll-outcome :num-faces 10 :value 2}
                      {:type :dice-roll-outcome :num-faces 10 :value 8}
                      {:type :dice-roll-outcome :num-faces 10 :value 5}
                      {:type :dice-roll-outcome :num-faces 10 :value 3}))

(def small-sample-rolls '({:type :dice-roll-outcome :num-faces 4 :value 1}
                             {:type :dice-roll-outcome :num-faces 4 :value 1}
                             {:type :dice-roll-outcome :num-faces 4 :value 4}))

(def sample-dice-input {:type :dice-input :num-faces 4 :num-rolls 3})

(def sample-dice-result {:type :dice-result :rolls small-sample-rolls})

(def sample-dice-outcome {:type :dice-roll-outcome :value 1})

;; Tests start -------

(deftest build-dice-input
  (testing "to verify the structure of dice-input"
    (is (= (data-struct/build-dice-input 3 4) {:type :dice-input 
                                               :num-faces 3
                                               :num-rolls 4}))))

(deftest build-dice-result
  (testing "to verify the structure of dice-result"
    (is (= (data-struct/build-dice-result sample-rolls) {:type :dice-result
                                                  :rolls sample-rolls}))))

(deftest build-dice-struct
  (testing "to verify the structure of dice-struct (i.e a structure with both input and result)"
    (is (= (data-struct/build-dice-struct sample-dice-input sample-dice-result) {:type :dice-struct
                                                                                 :input sample-dice-input
                                                                                 :result sample-dice-result}))))

(deftest build-dice-roll-outcome
  (testing "to verify the structure of dice-roll-outcome"
    (is (= (data-struct/build-dice-roll-outcome 1) {:type :dice-roll-outcome :value 1}))))
