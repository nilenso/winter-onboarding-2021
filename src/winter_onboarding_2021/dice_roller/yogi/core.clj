(ns winter-onboarding-2021.dice-roller.yogi.core
  (:require [winter-onboarding-2021.dice-roller.yogi.functions :as functions]))

;How to store - states?
;In clojure, like lisp, everything is a list.



;So, we can store the dice notation as a list - Or a list containing nested lists
;First we define basic operations - like keep, reroll, drop etc
; for example

;3d4k2


(functions/check '(2 1) 1)
;Now, lets say the expression is 3d4k2 we bind it to an identifer - theexp

(def theexp '(functions/k '(3 2)  (functions/my-roller 3 3)))


;We can tun eval on theexp, or, we can just print the binding
(print theexp)
(eval theexp)

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


