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

(def sample-dice-input {:num-faces 3 :num-rolls 4})

(def sample-dice-struct {:type :dice-struct
                         :num-faces 4
                         :num-rolls 3})

(def sample-less-than-selector {:op :< :x 10})

(def sample-dice-roll-outcome {:type :dice-roll-outcome :value 1})

(def sample-keep-operation {:op :k
                           :selector sample-less-than-selector})

(def sample-numeric-value {:type :numberic
                           :value 20})
;; Tests start -------

(deftest testing-data-struct
  (testing "to verify the structure of dice-input"
    (is (= sample-dice-input (data-struct/build-dice-input 3 4))))

  (testing "to verify the structure of dice-struct (i.e a structure with both input and result)"
    (is (=  sample-dice-struct (data-struct/build-dice-struct sample-dice-input))))

  (testing "to verify the structure of dice-roll-outcome"
    (is (= sample-dice-roll-outcome (data-struct/build-dice-roll-outcome 1))))

  (testing "to verify the structure of build selector"
    (is (= sample-less-than-selector (data-struct/build-selector :< 10))))

  (testing "to verify the structure of operation"
    (is (= sample-keep-operation (data-struct/build-operation :k sample-less-than-selector))))
  
  (testing "to verifythe structure of numeric values"
    (is (=  sample-numeric-value (data-struct/build-numberic-struct 10)))))


