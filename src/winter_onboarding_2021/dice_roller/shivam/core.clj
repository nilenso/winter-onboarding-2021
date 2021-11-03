(ns winter-onboarding-2021.dice-roller.shivam.core
  (:require [winter-onboarding-2021.dice-roller.shivam.data-structs :as data-structs]
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

;; Converts a list of Die into a flat list of numbers
(defn denormalise [die-values]
  (map :value die-values))

(defn take-lowest-value-dies [n coll]
  (take n (sort-by :value coll)))

(defn take-highest-value-dies [n coll]
  (take-last n (sort-by :value coll)))

(defn set-discarded-true [die-value]
  (assoc die-value :discarded true))

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

(defn update-first [selector update-fn [x & xs]]
  "Applies update-fn to only the first value which passes selector"
  (if (selector x)
    (conj xs (update-fn x))
    (conj (update-first selector update-fn xs) x)))

(defn discard-values [[v & vs] dice-rolls]
  "Sets the :discard flag to true of `dice-rolls` elements which match [v & vs]"
  (if (some? v)
    (discard-values vs
                    (update-first
                     #(and (= (:value v) (:value %)) (not (:discarded %)))
                     set-discarded-true
                     dice-rolls))
    dice-rolls))

(defn keep-values [[v & vs] dice-rolls]
  "Sets the :discard flag to false of `dice-rolls` elements which match [v & vs]"
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
  (assert (= type :dice))
  (let [{:keys [operation values num-rolls num-faces]} expression
        die-values (data-structs/generate-die-values num-rolls num-faces)
        operated-set (operate operation die-values)]
    {:operated operated-set
     :total 0})) ;; returns a hashmap with `total` and entities tree


(def selector (data-structs/build-selector :greater-than 2))

(def operation (data-structs/build-operation :keep selector))

(def dice-ast (data-structs/build-dice 3 4 operation))

(eval-dice-notation dice-ast)