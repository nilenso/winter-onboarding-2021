(ns winter-onboarding-2021.dice-roller.alisha.core
  (:require [winter-onboarding-2021.dice-roller.alisha.data_struct :as data-struct]
            [winter-onboarding-2021.dice-roller.alisha.core :as core]))

;;Sample Calls
(def dice-input (data-struct/build-dice-input 3 4))
(def selector (data-struct/build-selector :< 2))
(def operation (data-struct/build-operation :k selector))
(def dice-struct (data-struct/build-dice-struct dice-input operation))
(core/generate-rolls dice-struct)

;;Sample data struct
#_{:type :dice-struct,
   :num-faces 3,
   :num-rolls 4,
   :operation {:op :k, :selector {:op :<, :x 2}},
   :rolls ({:type :dice-roll-outcome, :value 2}
           {:type :dice-roll-outcome, :value 3}
           {:type :dice-roll-outcome, :value 1}
           {:type :dice-roll-outcome, :value 2})}
