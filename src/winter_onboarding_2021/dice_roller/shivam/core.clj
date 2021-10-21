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



(defn operate [operation raw-set] nil)

(defn eval-dice-notation [{type :type :as expression}]
  (when (= type "Dice")
    (let [{:keys [operation values]} expression
          operated-set (operate operation values)]
      ()))) ;; returns a hashmap with `total` and entities tree


;; 3d4kl2
(def selector (data-structs/build-selector "l" 2))

(def operation (data-structs/build-operation "k" selector))

(def rolled-dices (data-structs/generate-die-values 3 4))

(def dice-ast (data-structs/build-dice 3 4 rolled-dices operation))

(eval-dice-notation dice-ast)


;; (defn get-rolls [dice-info] (nil)) ;; return us [1 2 3]

;; (defn call-on-op [op condition num-set] (nil)) ; will call k, d, rr opeartion functions and then return the resultant vector
;; ((if (= op "k")
;;    (my-keep "3" num-set)) ;; if 
;;  (if (= op "d")
;;    (my-delete "l3" num-set))
;;  (if (= op "rr")))



;; (defn roll-dice [num-faces] (nil)) ; will return an Int in range (0, num-faces]


;; (defn hof [f] ((defn [condition digit my-set] (if (= condition "k")) ;; f must be a function like filter which filters out 
;;                  (f () my-set))))

;; (def my-keep (hof keep))
;; (def my-delete (hof remove))
;; (def my-reroll (hof (fn)))

;; (defn call-on-op [op condition num-set] (nil)) ; will call k, d, rr opeartion functions and then return the resultant vector
;; ((if (= op "k")
;;    (my-keep "3" num-set)) ;; if 
;;  (if (= op "d")
;;    (my-delete "l3" num-set))
;;  (if (= op "rr")))



;; (defn apply-condition [f condition])
;; (defn hh keep)


;; (defn apply-ops [op condition] (nil))