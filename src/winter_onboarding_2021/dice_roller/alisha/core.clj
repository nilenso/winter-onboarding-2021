(ns winter-onboarding-2021.dice-roller.alisha.core
  (:require [winter-onboarding-2021.dice-roller.alisha.data_struct :as data-struct]
            [winter-onboarding-2021.dice-roller.alisha.utils :as utils]))

(defn roll [num-faces] #(inc (rand-int num-faces)))

;;generate rolls
(defn generate-rolls [{:keys [num-rolls num-faces]}]
  (map #(data-struct/build-dice-roll-outcome %)
       (repeatedly num-rolls (roll num-faces))))

;;Sample Calls
#_(def dice-input (data-struct/build-dice-input 5 6))
#_(def rolls (generate-rolls {:num-rolls 5 :num-faces 6}))
#_(def dice-result (data-struct/build-dice-result rolls))
#_(data-struct/build-dice-struct dice-input dice-result)
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
