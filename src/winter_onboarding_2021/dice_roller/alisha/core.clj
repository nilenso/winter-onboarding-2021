(ns winter-onboarding-2021.dice-roller.alisha.core)

;;Define dice-roll-set expression representation
(defn dice-roll-set [values] {:type "DiceRollSet" :values (vec values)})

(defn dice-notation [rolls faces] {:type "DiceNotation" :rolls rolls :faces faces})

; op -> be "k", "d", "rr"
; selector -> "" "h" "l" "<" ">"
(defn set-operation [op selector] {:type "SetOperation" :op op :selector selector})
