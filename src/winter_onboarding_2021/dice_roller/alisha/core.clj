(ns winter-onboarding-2021.dice-roller.alisha.core)

;;Define dice-roll-set expression representation
;;(defn dice-roll-outcomes [values] (vec values))

(defn dice-notation [rolls faces] {:rolls rolls :faces faces})

; op -> be "k", "d", "rr"
; selector -> "" "h" "l" "<" ">"
(defn set-operation [op selector] {:op op :selector selector})

;;define set expression with consists of dice-notation and set-operation
(defn set-expression [dice-notation set-operation]
  {:dice-notation dice-notation
   :set-operation set-operation})

;;define
(defn perform-dice-roll [{:keys [rolls faces]}] 
  (vec (repeatedly rolls #(inc (rand-int faces)))))

(defn get-first-x-highest [values x] 
  (vec (take x (reverse (sort values)))))

(defn get-first-x-lowest [values x]
  (vec (take x (vec (sort values)))))

(defn get-greater-than-x [values x] 
  (vec (filter #(> %1 x) values)))

(defn get-lesser-than-x [values x]
  (vec (filter #(< %1 x) values)))

(defn get-equal-with-x [values x]
  (vec (filter #(= %1 x) values)))
