(ns winter-onboarding-2021.dice-roller.yogi.core)


;How to store - states?
;In clojure, like lisp, everything is a list.



;So, we can store the dice notation as a list - Or a list containing nested lists
;First we define basic operations - like keep, reroll, drop etc
; for example

;3d4k2

(defn my-roller [x y] (repeatedly x #(rand-int (+ y 1))))
(my-roller 5 3)
(defn check [x num] (if (= x num) true false) )
(defn k [x list] (filter (partial check x) list))
(defn d [x list] (filter (partial check x) list))

;Now, lets say the expression is 3d4k2 we bind it to an identifer - theexp
(def theexp '(k (l 2 (quote (3 2 2)))))

;We can tun eval on theexp, or, we can just print the binding
(print theexp)
(eval theexp)
(defn solve [list] (eval (conj (eval list) +)))
(solve theexp)

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


(defn rr [theexp] theexp)