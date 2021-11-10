(ns winter-onboarding-2021.dice-roller.alisha.core
  (:require [winter-onboarding-2021.dice-roller.alisha.data_struct :as data-struct])
  (:require [winter-onboarding-2021.dice-roller.alisha.utils :as utils]))

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

(defn selector-op [{:keys [op x]}
                   rolls]
  (let [wrapper-selector #(partial utils/append-selected %)]
    (case op
      :> (map (wrapper-selector (partial greater-than x)) rolls)
      :< (map (wrapper-selector (partial lesser-than x)) rolls)
      := (map (wrapper-selector (partial equals x)) rolls)
      :highest (utils/mark-rolls-selected (highest-x x rolls) (utils/mark-rolls-unselected rolls))
      :lowest (utils/mark-rolls-selected (lowest-x x rolls) (utils/mark-rolls-unselected rolls)))))

(defn keep [selector rolls]
  (let [rolls-after-selector-op (selector-op selector rolls)]
    (map utils/discard-if-not-selected rolls-after-selector-op)))

#_(keep {:op :highest :x 2} rolls)

(defn drop [selector rolls]
  (let [rolls-after-selector-op (selector-op selector rolls)]
    (map utils/discard-if-selected rolls-after-selector-op)))

#_(drop {:op :lowest :x 2} rolls)
