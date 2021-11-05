(ns winter-onboarding-2021.dice-roller.alisha-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.alisha.core :as dnd-dice]
            [winter-onboarding-2021.dice-roller.alisha.data_struct :as data-struct]))

(def sample-outcomes '({:type :dice-roll-outcome :num-faces 10 :value 6}
                      {:type :dice-roll-outcome :num-faces 10 :value 1}
                      {:type :dice-roll-outcome :num-faces 10 :value 2}
                      {:type :dice-roll-outcome :num-faces 10 :value 8}
                      {:type :dice-roll-outcome :num-faces 10 :value 5}
                      {:type :dice-roll-outcome :num-faces 10 :value 3}))

(def small-sample-outcomes '({:type :dice-roll-outcome :num-faces 4 :value 1}
                             {:type :dice-roll-outcome :num-faces 4 :value 1}
                             {:type :dice-roll-outcome :num-faces 4 :value 4}))
