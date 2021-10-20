(ns winter-onboarding-2021.dice-roller.alisha.core)

;;Define dice-roll-set expression representation
(defn dice-roll-set [values] {:type "DiceRollSet" :values (vec values)})
