(ns winter-onboarding-2021.dice-roller.shivam.core
  (:require [clojure.pprint :as pprint]
            [winter-onboarding-2021.dice-roller.shivam.data-structs :as data-structs]))

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

(defn check [criteria my-num]
  (contains? criteria my-num))

(defn generate-operation-handler [op]
  (fn [my-set condition number]
    (case condition
      "" (op #(when (= % number) %) my-set) ;; f must be a function like filter which passes each element from my-set to fn. fn will return true or false. On that 
      "<" (op #(when (< % number) %) my-set)
      ">" (op #(when (> % number) %) my-set)
      "l" (op #(when (check (vec (take number (sort my-set))) %) %) my-set)
      "h" (op #(when (check (vec (take-last number (sort my-set))) %) %) my-set))))

(def keep-in-set (generate-operation-handler keep))
;; (def drop-in-set (generate-operation-handler remove))

;; func `operate` will return us a new set where op(with category on number) is applied to raw-set
(defn operate [[[op] [category number]] raw-set]
  (case op
    "k" (keep-in-set raw-set category number)))

(defn eval-dice-notation [{type :type :as expression}]
  (when (= type "Dice")
    (let [{:keys [operation values]} expression
          operated-set (operate operation values)]
      {:original values
       :operated operated-set
       :total 0}))) ;; returns a hashmap with `total` and entities tree


;; 3d4kl2
(def selector (data-structs/build-selector "l" 2))

(def operation (data-structs/build-operation "k" selector))

(def rolled-dices (data-structs/generate-die-values 3 4))

(def dice-ast (data-structs/build-dice 3 4 rolled-dices operation))

(eval-dice-notation dice-ast)







;; (def my-delete (generate-operation-handler remove))