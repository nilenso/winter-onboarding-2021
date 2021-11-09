(ns winter-onboarding-2021.dice-roller.yogi.operations
  (:require [winter-onboarding-2021.dice-roller.yogi.selectors :as selectors]))

(defn apply-selector [dice-model]
  (case ((dice-model :selector) :selector-type)
    :greater (selectors/greater ((dice-model :selector) :value) (last (dice-model :states)))
    :lower (selectors/lower ((dice-model :selector) :value) (last (dice-model :states)))
    :highest (selectors/highest ((dice-model :selector) :value) (last (dice-model :states)))
    :lowest (selectors/lowest ((dice-model :selector) :value) (last (dice-model :states)))
    :equal-to (selectors/equal ((dice-model :selector) :value) (last (dice-model :states)))))


(defn my-drop [dice-model]
  (assoc dice-model :states
         (conj
          (dice-model :states)
          (remove (partial contains?
                           (set (apply-selector dice-model)))
                  (last (dice-model :states))))
         :value (reduce + (last (dice-model :states) ))))


(defn my-keep [dice-model]
  (assoc dice-model :states
         (conj
          (dice-model :states)
          (apply-selector dice-model))
         :value (reduce + (last (dice-model :states)))))



(defn reroll [dice-model]
  (if (or (> (count (dice-model :states))
             (dice-model :num-dice))
          (= 0 (count (set (apply-selector dice-model)))))
    (do
      (print "reroll limit reached or reroll condition met")
      dice-model)
    (reroll (assoc dice-model :states
                   (conj (dice-model :states)
                         (map (partial
                               (fn [x y] (if (contains? x y) (+ 1 (rand-int (dice-model :num-faces))) y))
                               (set (apply-selector dice-model)))
                              (last (dice-model :states))))))))








