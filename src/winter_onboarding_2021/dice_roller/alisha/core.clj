(ns winter-onboarding-2021.dice-roller.alisha.core
  (:require [winter-onboarding-2021.dice-roller.alisha.data_struct :as data-struct])
  (:require [winter-onboarding-2021.dice-roller.alisha.utils :as utils])
  (:require [winter-onboarding-2021.dice-roller.alisha.operations :as operations])
  )

(defn generate-rolls [dice-struct]
  (let [num-rolls (:num-rolls dice-struct)
        num-faces (:num-faces dice-struct)
        rolls (map #(data-struct/build-dice-roll-outcome %)
                   (repeatedly num-rolls (partial utils/roll num-faces)))]
    (assoc dice-struct :rolls rolls)))

(defn eval-rolls [rolls]
  (letfn [(add-non-discared-dice-outcomes [accum roll-outcome]
            (if (and (not= (:discarded roll-outcome) nil) (:discarded roll-outcome))
              (+ accum 0)                        ;;discarded is true
              (+ accum (:value roll-outcome))))] ;;not discarded & no discard prop(incase of reroll) 
    (reduce add-non-discared-dice-outcomes 0 rolls)))

(defn add-rolls-to-dice-struct [dice-struct]
  (let [dice-struct-with-rolls (generate-rolls dice-struct)]
    (if (:operation dice-struct-with-rolls)
      (update dice-struct-with-rolls :rolls (partial operations/operate dice-struct))
      dice-struct-with-rolls)))

(defn add-value-to-dice-struct [{:keys [rolls] :as dice-struct}]
  (assoc dice-struct :value (eval-rolls rolls)))

(defn eval-dice-struct [dice-struct]
  (-> dice-struct
      add-rolls-to-dice-struct
      add-value-to-dice-struct))

#_(def dice-struct {:type :dice-struct
                  :num-faces 4
                  :num-rolls 5
                  :operation {:op :reroll
                              :selector {:op :=
                                         :x 2}}})

#_(eval-dice-struct dice-struct)
