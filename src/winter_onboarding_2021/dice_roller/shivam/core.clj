(ns winter-onboarding-2021.dice-roller.shivam.core)


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
(defn expression [subtree] {:type "Expression" :subtree subtree :total total :set set})


(defn build-die [num-faces value]
  {:type "Die"
  :num-faces num-faces ;; Int, number of faces this die should have
  :value value})


;; Dice represents a set of Die
(defn build-dice [num-rolls num-faces values operations]
  {:type "Dice"
   :num-rolls num-rolls 
   :num-faces num-faces
   :values values ;; Vector of `Die` 
   :operations operations})

;; Represents Unary Operation
;; op tells us whether it is - or +
(defn build-unary-op [op value]
  {:type "UnaryOp"
   :op op
   :value value})

;; Represents Binary Operations like -, +, *, /
;; left and right represents the expressions on which the operartion will be applied
(defn build-bin-op [left op right]
  {:type "BinaryOp"
   :left left
   :op op ;; 
   :right right})

;; Represents Set Operation
(defn build-operation [op selector]
  {:type "SetOperation"
   :op op ;; k, rr, d
   :selector selector})

;; Represents Set Selector
;; `category` can be "", "<", ">", "l", "h" 
(defn build-selector [category num] 
  {:type "SetSelector"
   :category category
   :num num })


(defn eval-dice-notation [expression] (nil)) ;; returns a hashmap with `total` and entities tree




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