(ns winter-onboarding-2021.dice-roller.alisha.core
  (:require [winter-onboarding-2021.dice-roller.alisha.data_struct :as data-struct]))

(defn roll [num-faces] #(inc (rand-int num-faces)))

;;generate rolls
(defn generate-rolls [dice-struct]
  (let [num-rolls (:num-rolls dice-struct)
        num-faces (:num-faces dice-struct)
        rolls (map #(data-struct/build-dice-roll-outcome %)
                   (repeatedly num-rolls (roll num-faces)))]
    (assoc dice-struct :rolls rolls)))

(defn equals [x roll-outcome]
  (= (:value roll-outcome) x))

(defn lesser-than [x roll-outcome]
  (< (:value roll-outcome) x))

(defn greater-than [x roll-outcome]
  (> (:value roll-outcome) x))

(defn highest-x [x rolls]
  (take x (reverse (sort-by :value rolls))))

(defn lowest-x [x rolls]
  (take x (sort-by :value rolls)))
