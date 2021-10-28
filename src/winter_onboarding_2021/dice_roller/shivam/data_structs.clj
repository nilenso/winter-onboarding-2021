(ns winter-onboarding-2021.dice-roller.shivam.data-structs
  (:require [winter-onboarding-2021.dice-roller.shivam.utils :as utils]))

(defn build-die [num-faces value]
  (assert (>= num-faces value) "`value` can't be larger than `num-faces`")
  {:type :die
   :num-faces num-faces
   :value value})

;; Generate list of Die with length equal to `num-rolls`
(defn generate-die-values [num-rolls num-faces]
  (map
   (partial build-die num-faces)
   (repeatedly num-rolls #(utils/gen-rand-int num-faces))))

;; Dice represents a set of Die
(defn build-dice [num-rolls num-faces operation]
  {:type :dice
   :num-rolls num-rolls
   :num-faces num-faces
   :operation operation})

;; Represents Unary Operation
;; op tells us whether it is - or +
(defn build-unary-op [op value]
  {:type :unary-op
   :op op
   :value value})

;; Represents Binary Operations like -, +, *, /
;; left and right represents the expressions on which the operartion will be applied
(defn build-bin-op [left op right]
  {:type :binary-op
   :left left
   :op op
   :right right})

;; Represents Set Operation
(defn build-operation [op selector]
  (assert (some #(= % op) utils/valid-ops) "Invalid Operator")
  {:type :set-operation
   :op op ;; k, rr, d
   :selector selector})

;; Represents Set Selector
;; `criteria` can be "", "<", ">", "l", "h" 
(defn build-selector [criteria num]
  (assert (some #(= % criteria) utils/valid-selectors) "Invalid Selector")
  {:type :set-selector
   :criteria criteria
   :num num})