(ns winter-onboarding-2021.dice-roller.alisha.core)

(defn dice-notation [num-rolls num-faces] {:num-rolls num-rolls :num-faces num-faces})

; op -> be "k", "d", "rr"
; selector -> "" "h" "l" "<" ">"
(defn set-operation [op selector] {:op op :selector selector})

;;define set expression with consists of dice-notation and set-operation
(defn set-expression [dice-notation set-operation]
  {:dice-notation dice-notation
   :set-operation set-operation})

(defn generate-dice-roll-outcome [value num-faces]
  {:type :dice-roll-outcome
   :num-faces num-faces
   :value value})

(defn perform-dice-roll [{:keys [num-rolls num-faces]}]
  (map #(generate-dice-roll-outcome % num-faces)
       (repeatedly num-rolls #(inc (rand-int num-faces)))))

;;Selector functions 
(defn get-x-highest [outcomes x]
  (take x (reverse (sort-by :value outcomes))))

(defn get-x-lowest [outcomes x]
  (take x (vec (sort-by :value outcomes))))

(defn get-greater-than-x [outcomes x]
  (filter #(> (get %1 :value) x) outcomes))

(defn get-lesser-than-x [outcomes x]
  (filter #(< (get %1 :value) x) outcomes))

(defn get-equal-with-x [outcomes x]
  (filter #(= (get %1 :value) x) outcomes))

(defn keep-from-dice-outcomes [selector-func outcomes x]
  (selector-func outcomes x))

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

(defn drop-from-dice-outcomes [selector-operation-func outcomes x]
  (let [to-be-dropped-outcomes (selector-operation-func outcomes x)]
    ;;(substract on sequence)
    (reduce (fn [outcomes, ele]
              (remove-once #(= %1 ele) outcomes))
            outcomes
            to-be-dropped-outcomes)))

#_(defn reroll-dice [selector-operation-func outcomes x dice-notation]
  (if (not= (selector-operation-func outcomes x) [])
    (let [new-outcomes (perform-dice-roll dice-notation)]
      (reroll-dice selector-operation-func new-outcomes x dice-notation))
     outcomes))
