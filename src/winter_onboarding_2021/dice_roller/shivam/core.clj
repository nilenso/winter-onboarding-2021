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


;; Converts a list of Die into a flat list of numbers
(defn denormalise [die-values]
  (map :value die-values))

(defn take-lowest [n coll]
  (take n (sort coll)))

(defn take-highest [n coll]
  (take-last n (sort coll)))

(defn keep-op [pred-or-coll raw-set]
  (if (fn? pred-or-coll)
    (keep pred-or-coll raw-set)
    pred-or-coll))

(defn remove-op [pred-or-coll raw-set]
  (if (fn? pred-or-coll)
    (remove pred-or-coll raw-set)
    (reduce (fn [accum, ele] (utils/remove-once #(= % ele) accum))
            raw-set
            pred-or-coll)))

#_(defn reroll-op [pred-or-coll raw-set]
  (if (fn? pred-or-coll)
    nil
    nil))

(defn generate-operation-handler [op]
  (fn [my-set criteria number]
    (case criteria
      "" (op #(when (= % number) %) my-set)
      "<" (op #(when (< % number) %) my-set)
      ">" (op #(when (> % number) %) my-set)
      "l" (op (take-lowest number my-set) my-set)
      "h" (op (take-highest number my-set) my-set))))

(def keep-in-set (generate-operation-handler keep-op))

(def drop-in-set (generate-operation-handler remove-op))
;; (def reroll-in-set (generate-operation-handler reroller))

;; func `operate` will return us a new set where op(with criteria on number) is applied to raw-set
(defn operate [{:keys [op selector]} die-values]
  (let [{:keys [criteria num]} selector raw-set (denormalise die-values)]
    (case op
      "k" (keep-in-set raw-set criteria num)
      "d" (drop-in-set raw-set criteria num))))

(defn eval-dice-notation [{type :type :as expression}]
  (when (= type "Dice")
    (let [{:keys [operation values]} expression
          operated-set (operate operation values)]
      {:original values
       :operated operated-set
       :total 0}))) ;; returns a hashmap with `total` and entities tree


(def selector (data-structs/build-selector ">" 2))

(def operation (data-structs/build-operation "k" selector))

(def die-values (data-structs/generate-die-values 3 4))

(def dice-ast (data-structs/build-dice 3 4 die-values operation))

(eval-dice-notation dice-ast)
