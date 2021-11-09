(ns winter-onboarding-2021.dice-roller.shivam.core
  (:require [winter-onboarding-2021.dice-roller.shivam.models :as models]
            [winter-onboarding-2021.dice-roller.shivam.utils :as utils]))

;; Some sample dice expressions
;; 2d11k2 -> Roll two dices with 11 faces and keep all 2s
;; 3d3d>2 -> Roll three dices with 3 faces and delete all numbers greater than 2
;; 16d9rrl8 -> Roll sixteen dices with 9 faces and reroll lowest 8 values

;; Possible Dice notations
;; 3d2
;; 9d11
;; 5d2
;; 7d8
;; 12d12
;; 4d(d6-1)kl

;; Possible Set Operations
;; k
;; d
;; rr

;; Possible matching expressions
;; 8
;; l8
;; h8
;; >8
;; <8

;; Expression -- it will be the root element
;; (defn expression [subtree] {:type "Expression" :subtree subtree :total total :set set})

(def max-number-of-rerolls 500)

(defn take-lowest-value-dies [n coll]
  (take n (sort-by :value coll)))

(defn take-highest-value-dies [n coll]
  (take-last n (sort-by :value coll)))

(defn set-discarded-true [die-value]
  (assoc die-value :discarded true))

(defn sum-non-discarded [values]
  (reduce #(if (:discarded %2)
             %1
             (+ %1 (:value %2)))
          0
          values))

(defn update-current-value
  "die-value: Die 
   new-value: Int

   Shifts the current (:value die-value) to :previous-values and 
   replaces (:value die-value) with `new-value`
   "
  [{:keys [value previous-values] :as die-value} new-value]
  (assoc die-value
         :previous-values (conj previous-values value)
         :value new-value))

(defn update-first 
  "Applies update-fn to only the first value which passes selector"
  [selector update-fn [x & xs]]
  (if (selector x)
    (conj xs (update-fn x))
    (conj (update-first selector update-fn xs) x)))

(defn discard-values 
  "Sets the :discard flag to true of `dice-rolls` elements which match [v & vs]"
  [[v & vs] dice-rolls]
  (if (some? v)
    (discard-values vs
                    (update-first
                     #(and (= (:value v) (:value %)) (not (:discarded %)))
                     set-discarded-true
                     dice-rolls))
    dice-rolls))

(defn keep-values
  "Sets the :discard flag to false of `dice-rolls` elements which match [v & vs]"
  [[v & vs] dice-rolls]
  (if (some? v)
    (keep-values vs
                 (update-first
                  #(and (= (:value v) (:value %)) (:discarded %))
                  #(assoc % :discarded false)
                  dice-rolls))
    dice-rolls))

;; keep-op returns a list of Die
(defn keep-op [pred die-values] ;; array of Die 
  (if (fn? pred)
    (map #(if (pred (:value %))
            %
            (set-discarded-true %))
         die-values)
    (keep-values pred (map set-discarded-true die-values))))

;; remove-op returns a list of Die
(defn remove-op [pred die-values]
  (if (fn? pred)
    (map #(if (pred (:value %))
            (set-discarded-true %)
            %)
         die-values)
    (discard-values pred die-values)))

(defn reroll-op [pred die-values]
  (letfn [(gen-valid-rand-value [repeated-rolls die-value]
            (assert (<= repeated-rolls max-number-of-rerolls) "Limit exceeded!!!")
            (if (fn? pred)
              (if (pred (:value die-value))
                (gen-valid-rand-value
                 (inc repeated-rolls)
                 (update-current-value die-value (utils/gen-rand-int (:num-faces die-value))))
                die-value)
              (if (some #(= % (:value die-value)) pred)
                (gen-valid-rand-value
                 (inc repeated-rolls)
                 (update-current-value die-value (utils/gen-rand-int (:num-faces die-value))))
                die-value)))]
    (map (partial gen-valid-rand-value 0) die-values)))

(defn generate-operation-handler [operator]
  (fn [die-values criteria number]
      (case criteria
        :equals (operator #(when (= % number) %) die-values)
        :lesser-than (operator #(when (< % number) %) die-values)
        :greater-than (operator #(when (> % number) %) die-values)
        :lowest (operator (take-lowest-value-dies number die-values) die-values)
      :highest (operator (take-highest-value-dies number die-values) die-values))))

(def keep-in-set (generate-operation-handler keep-op))

(def drop-in-set (generate-operation-handler remove-op))

(def reroll-in-set (generate-operation-handler reroll-op))

;; func `operate` will return us a new set where op(with criteria on number) is applied to raw-set
(defn operate [{:keys [op selector]} die-values]
  (let [{:keys [criteria num]} selector]
    (case op
      :keep (keep-in-set die-values criteria num)
      :drop (drop-in-set die-values criteria num)
      :reroll (reroll-in-set die-values criteria num))))

(defn eval-dice-notation [{type :type :as expression}]
  {:pre [(= type :dice)]}
  (let [{:keys [operation num-rolls num-faces]} expression
        die-values (models/generate-die-values num-rolls num-faces)
        operated-values (operate operation die-values)
        value (sum-non-discarded operated-values)]
    {:type :evaluated-dice
     :values operated-values
     :value value})) ;; returns a hashmap with `total` and entities tree

(declare evaluate-by-type)

(defn eval-bin-op [{:keys [type] :as bin-op-exp}]
  {:pre [(= type :binary-op)]}
  (let [{:keys [left op right]} bin-op-exp
        evaluated-left (evaluate-by-type left)
        evaluated-right (evaluate-by-type right)
        value (case op
                :add (+ (:value evaluated-left) (:value evaluated-right))
                :subtract (- (:value evaluated-left) (:value evaluated-right))
                :multiply (* (:value evaluated-left) (:value evaluated-right))
                :divide (/ (:value evaluated-left) (:value evaluated-right)))]
    (assoc bin-op-exp
           :type :evaluated-bin-op
           :left evaluated-left
           :right evaluated-right
           :value value)))

; The `reroll` operation can't be applied to a Set
; because a Set is not a Dice i.e. Set doesnt have `num-faces`
(defn eval-set [{:keys [type] :as st}]
  {:pre [(= type :set)]}
  (let [{:keys [values operation]} st
        evaluated-values (map evaluate-by-type values)
        ;; if operation is nil, return values otherwise apply only allowed operations
        operated-values (if (nil? operation)
                          evaluated-values
                          (do (assert (some #(= (get-in st [:operation :op]) %) [:keep :drop])
                                    "Only keep or drop operation is allowed on a Set")
                            (operate operation evaluated-values)))
        value (sum-non-discarded operated-values)]
    {:type :evaluated-set
     :values operated-values
     :value value}))

(defn evaluate-by-type 
  "If entity has :value, it means it's already evaluated"
  [entity]
  (if (contains? entity :value)
    entity
    (case (:type entity)
      :dice (eval-dice-notation entity)
      :binary-op (eval-bin-op entity)
      :literal entity
      :set (eval-set entity))))

(def selector (models/build-selector :greater-than 2))

(def operation (models/build-operation :keep selector))

(def dice-ast (models/build-dice 3 4 operation))

(def bin-op (models/build-bin-op
             (models/build-literal 3)
             :add
             dice-ast))

(def st (models/build-set
         [(models/build-literal 2)
         (models/build-literal 4)
         (models/build-literal 10)]
         operation))

(eval-set st)

(eval-bin-op bin-op)

(eval-dice-notation dice-ast)

