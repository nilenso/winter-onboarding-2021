(ns winter-onboarding-2021.dice-roller.shivam.models
  (:require [winter-onboarding-2021.dice-roller.shivam.utils :as utils]))

(defn build-literal [value]
  {:pre [(number? value)]}
  {:type :literal
   :value value
   :discarded false
   :previous-values []})

(defn build-die [num-faces value]
  #_(assert (>= num-faces value) "`value` can't be larger than `num-faces`")
  {:pre [(>= num-faces value)]}
  {:type :die
   :num-faces num-faces
   :value value
   :discarded false
   :previous-values []})

;; Generate list of Die with length equal to `num-rolls`
(defn generate-die-values [num-rolls num-faces]
  (map
   (partial build-die num-faces)
   (repeatedly num-rolls #(utils/gen-rand-int num-faces))))

;; Dice represents a set of Die
(defn build-dice [num-rolls num-faces operation]
  {:type :dice
   :num-rolls num-rolls ;; {:type unary-op :value {type: dice } :op :plus}
   :num-faces num-faces
   :operation operation})

(defn build-set [values operation]
  {:type :set
   :values values
   :operation operation})

;; Represents Unary Operation
;; op tells us whether it is - or +
(defn build-unary-op [op value]
  {:type :unary-op
   :op op ;; :minus, :plus
   :value value})

;; Represents Binary Operations like -, +, *, /
;; left and right represents the expressions on which the operartion will be applied
(defn build-bin-op [left op right]
  {:type :binary-op
   :left left
   :op op ;; :add, :subtract, :multiply, :divide
   :right right})

;; Represents Set Operation
(defn build-operation [op selector]
  {:pre [(some #(= % op) utils/valid-ops)]}
  #_(assert (some #(= % op) utils/valid-ops) "Invalid Operator")
  {:type :set-operation
   :op op ;; k, rr, d
   :selector selector})

;; Represents Set Selector
;; `criteria` can be "", "<", ">", "l", "h" 
(defn build-selector [criteria num]
  {:pre [(some #(= % criteria) utils/valid-selectors)]}
  #_(assert (some #(= % criteria) utils/valid-selectors) "Invalid Selector")
  {:type :set-selector
   :criteria criteria
   :num num})
