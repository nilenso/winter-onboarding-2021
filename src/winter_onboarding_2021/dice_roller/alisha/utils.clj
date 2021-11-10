(ns winter-onboarding-2021.dice-roller.alisha.utils)

(defn set-selected-false [dice-outcome]
  (assoc dice-outcome :selected false))

(defn set-selected-true [dice-outcome]
  (assoc dice-outcome :selected true))

(defn mark-rolls-unselected [rolls]
  (map set-selected-false rolls))

(defn append-selected [selector-fn roll-outcome]
  (if (selector-fn roll-outcome)
    (set-selected-true roll-outcome)
    (set-selected-false roll-outcome)))
