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
  ([dice-roller]
   (assoc dice-roller :states (conj (dice-roller :states) 
                                    (remove (partial contains? 
                                                     (set (case ((dice-roller :selector) :selector-type)
                                                                              :greater (selectors/greater ((dice-roller :selector) :value) ((dice-roller :states) 1))
                                                                              :lower (selectors/lower ((dice-roller :selector) :value) ((dice-roller :states) 1))
                                                                              :highest (selectors/highest ((dice-roller :selector) :value) ((dice-roller :states) 1))
                                                                              :lowest (selectors/lowest ((dice-roller :selector) :value) ((dice-roller :states) 1))
                                                                              :equal-to (selectors/equal ((dice-roller :selector) :value) ((dice-roller :states) 1)))))
                                                    ((dice-roller :states) 1))))))


(defn my-keep
  ([dice-roller]
   (assoc dice-roller :states (conj (dice-roller :states) (case ((dice-roller :selector) :selector-type)
                                                            :greater (selectors/greater ((dice-roller :selector) :value) ((dice-roller :states) 1))
                                                            :lower (selectors/lower ((dice-roller :selector) :value) ((dice-roller :states) 1))
                                                            :highest (selectors/highest ((dice-roller :selector) :value) ((dice-roller :states) 1))
                                                            :lowest (selectors/lowest ((dice-roller :selector) :value) ((dice-roller :states) 1))
                                                            :equal-to (selectors/equal ((dice-roller :selector) :value) ((dice-roller :states) 1)))))))


(defn reroll [dice-roller]
  )

(reroll (my-roller 3 4))

