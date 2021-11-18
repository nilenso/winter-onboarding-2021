(ns winter-onboarding-2021.dice-roller.alisha-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.alisha.core :as dnd-dice]
            [winter-onboarding-2021.dice-roller.alisha.data_struct :as data-struct]
            [winter-onboarding-2021.dice-roller.alisha.selector :as selector]))

(def sample-rolls '({:type :dice-roll-outcome :value 6}
                    {:type :dice-roll-outcome :value 1}
                    {:type :dice-roll-outcome :value 2}
                    {:type :dice-roll-outcome :value 8}
                    {:type :dice-roll-outcome :value 5}
                    {:type :dice-roll-outcome :value 3}))

(def small-sample-rolls '({:type :dice-roll-outcome :value 1}
                          {:type :dice-roll-outcome :value 1}
                          {:type :dice-roll-outcome :value 4}))

(def sample-dice-input {:num-faces 3 :num-rolls 4})

(def sample-dice-struct {:type :dice-struct
                         :num-faces 3
                         :num-rolls 4})

;;---sample selectors

(def sample-less-than-selector {:op :< :x 3})

(def sample-greater-than-selector {:op :> :x 3})

(def sample-highest-x-selector {:op :highest :x 2})

;;----
(def sample-dice-roll-outcome {:type :dice-roll-outcome :value 1})

(def sample-keep-operation {:op :k
                            :selector sample-less-than-selector})

(def sample-numeric-value {:type :numeric
                           :value 20})

;;--Tests

(deftest data-struct
  (testing "to verify the structure of dice-input"
    (is (= sample-dice-input (data-struct/build-dice-input 3 4))))

  (testing "to verify the structure of dice-struct (i.e a structure with both input and result)"
    (is (=  sample-dice-struct (data-struct/build-dice-struct sample-dice-input))))

  (testing "to verify the structure of dice-roll-outcome"
    (is (= sample-dice-roll-outcome (data-struct/build-dice-roll-outcome 1))))

  (testing "to verify the structure of build selector"
    (is (= sample-less-than-selector (data-struct/build-selector :< 3))))

  (testing "to verify the structure of operation"
    (is (= sample-keep-operation (data-struct/build-operation :k sample-less-than-selector))))

  (testing "to verifythe structure of numeric values"
    (is (=  sample-numeric-value (data-struct/build-numberic-struct 20)))))

(deftest selectors
  (testing "equals selector checks equality of the value of dice-roll-outcome with x"
    (is (= true (selector/equals 1 sample-dice-roll-outcome)))
    (is (= false (selector/equals 2 sample-dice-roll-outcome))))

  (testing "lesser-than selector checks if the value of dice-roll-outcome is less than x"
    (is (= true (selector/lesser-than 3 sample-dice-roll-outcome)))
    (is (= false (selector/lesser-than 1 sample-dice-roll-outcome))))

  (testing "greater-than selector checks if the value of dice-roll-outcome is greater than x"
    (is (= true (selector/greater-than 0 sample-dice-roll-outcome)))
    (is (= false (selector/greater-than 2 sample-dice-roll-outcome))))

  (testing "highest-x selector returns the x highest dice-roll-outcomes from rolls"
    (is (= '({:type :dice-roll-outcome :value 4})
           (selector/highest-x 1 small-sample-rolls))))

  (testing "lowest-x selector returns the x highest dice-roll-outcomes from rolls"
    (is (= '({:type :dice-roll-outcome :value 1}
             {:type :dice-roll-outcome :value 1})
           (selector/lowest-x 2 small-sample-rolls))))
  
  (testing "selector-op"
    (is (= '({:type :dice-roll-outcome :value 1 :selected true}
             {:type :dice-roll-outcome :value 1 :selected true}
             {:type :dice-roll-outcome :value 4 :selected false})
           (selector/selector-op sample-less-than-selector small-sample-rolls)))
    (is (= '({:type :dice-roll-outcome :value 1 :selected false}
             {:type :dice-roll-outcome :value 1 :selected false}
             {:type :dice-roll-outcome :value 4 :selected true})
           (selector/selector-op sample-greater-than-selector small-sample-rolls)))
    (is (= '({:type :dice-roll-outcome :value 1 :selected true} 
             {:type :dice-roll-outcome :value 1 :selected false}
             {:type :dice-roll-outcome :value 4 :selected true})
           (selector/selector-op sample-highest-x-selector small-sample-rolls)))))
