(ns winter-onboarding-2021.dice-roller.yogi.core
  (:require [winter-onboarding-2021.dice-roller.yogi.functions :as functions]))

;How to store - states?
;In clojure, like lisp, everything is a list.



;So, we can store the dice notation as a list - Or a list containing nested lists
;First we define basic operations - like keep, reroll, drop etc
; for example

;3d4k2

(def op ())
(conj op 3)
(conj op 4)

;Now, lets say the expression is 3d4k2 we bind it to an identifer - theexp

;(def theexp '(functions/k '(3 2)  (conj op (functions/my-roller 3 3))))
(def op (concat
  '(functions/k '(3 2))
  (functions/my-roller 3 3)))

;3d5kl2
;first evaluate NdX expression, store it on stack maybe
;k,d are impure functions - pop NdX from stack when used
;l2 h2 are pure functions - keep NdX in stack

(functions/my-roller 3 5)

(filter even? [1 2 3 5 6 ])
(functions/keep (functions/lowest 2 (functions/my-roller 3 5)) (functions/my-roller 3 5))
(def dice (functions/my-roller 5 6))
(def lown (functions/lowest 2 dice))
(functions/keep lown dice)
(+ (reduce + (functions/my-roller 4 5)) 2)

;We can tun eval on theexp, or, we can just print the binding
(print theexp)
(eval theexp)
(op)
(functions/solve theexp)
;
;;
;
;;
;
;
;
;
;
;


