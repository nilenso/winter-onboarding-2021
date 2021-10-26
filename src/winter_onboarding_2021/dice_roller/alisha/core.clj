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

;;Selector functions 
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

;;define a function keep
;;it takes a vector of values (i.e values)
;;it takes another vector which is subset of values

;; sample call -> (keep-fn get-greater-than-x [1 2 3 4 5 2] 2)
(defn keep-from-dice-outcomes [selector-operation-func values x]
  (selector-operation-func values x))

;;helper function to remove once from a collection satisfying a predicate 
(defn remove-once [pred coll]
  ((fn inner [coll]
     (lazy-seq
      (when-let [[x & xs] (seq coll)]
        (if (pred x)
          xs
          (cons x (inner xs)))
        )))
   coll))

(defn drop-from-dice-outcomes [selector-operation-func values x]
  (let [to-be-dropped-vals (selector-operation-func values x)]
    ;;(substract on vectors)
    (reduce (fn [outcomes, ele]
              (remove-once #(= %1 ele) outcomes)) values to-be-dropped-vals)))
