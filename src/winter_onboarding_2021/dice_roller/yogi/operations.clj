(ns winter-onboarding-2021.dice-roller.yogi.operations
  (:require [winter-onboarding-2021.dice-roller.yogi.selectors :as selectors]))

(defn my-roller [x y]
  (repeatedly x #(+ 1 (rand-int y))))


(defn check [x list]
  (if (seq? x)
    (contains? (set x) list)
    (= x list)))

(defn rcheck [x list]
  (if (seq? x)
    (not (contains? (set x) list))
    (not (= x list))))

(defn my-drop
  ([dice-model]
   (assoc dice-model :states (conj (dice-model :states)
                                    (remove (partial contains?
                                                     (set (case ((dice-model :selector) :selector-type)
                                                            :greater (selectors/greater ((dice-model :selector) :value) ((dice-model :states) 1))
                                                            :lower (selectors/lower ((dice-model :selector) :value) ((dice-model :states) 1))
                                                            :highest (selectors/highest ((dice-model :selector) :value) ((dice-model :states) 1))
                                                            :lowest (selectors/lowest ((dice-model :selector) :value) ((dice-model :states) 1))
                                                            :equal-to (selectors/equal ((dice-model :selector) :value) ((dice-model :states) 1)))))
                                            ((dice-model :states) 1))))))


(defn my-keep
  ([dice-model]
   (assoc dice-model :states (conj (dice-model :states) (case ((dice-model :selector) :selector-type)
                                                            :greater (selectors/greater ((dice-model :selector) :value) ((dice-model :states) 1))
                                                            :lower (selectors/lower ((dice-model :selector) :value) ((dice-model :states) 1))
                                                            :highest (selectors/highest ((dice-model :selector) :value) ((dice-model :states) 1))
                                                            :lowest (selectors/lowest ((dice-model :selector) :value) ((dice-model :states) 1))
                                                            :equal-to (selectors/equal ((dice-model :selector) :value) ((dice-model :states) 1)))))))




(defn reroll [dice-model]
  (map (partial (fn [x y] (if (contains? x y) (rand-int (dice-model :num-faces)) y)) #{0 1})  [0 1 3]))


3d10rr<3  




