(ns winter-onboarding-2021.dice-roller.alisha.core
  (:require [winter-onboarding-2021.dice-roller.alisha.data_struct :as data-struct]
            [winter-onboarding-2021.dice-roller.alisha.core :as core]))

;;Sample Calls
(def dice-input (data-struct/build-dice-input 5 6))
(def rolls (core/generate-rolls {:num-rolls 5 :num-faces 6}))
(def dice-result (data-struct/build-dice-result rolls))
(data-struct/build-dice-struct dice-input dice-result)


;;Sample data struct
#_{:input {:type :dice-input, :num-faces 5, :num-rolls 6}
   :result
   {:type :dice-result
    :rolls
    ({:type :dice-roll-outcome, :value 2}
     {:type :dice-roll-outcome, :value 4}
     {:type :dice-roll-outcome, :value 5}
     {:type :dice-roll-outcome, :value 3}
     {:type :dice-roll-outcome, :value 4})}}
