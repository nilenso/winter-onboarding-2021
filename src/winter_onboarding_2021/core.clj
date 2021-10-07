(ns winter-onboarding-2021.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(foo "")

(+ 1 1)

(print (str 1 2 3))

(print (+ 1 3) (+ 1 2 3) 4)

(defn selfie
  "Given a function `f`, return the result of
  applying `f` to itself."
  [f]
  (f f))

(= 42
   (identity 42)
   ((selfie identity) 42)
   ((selfie (selfie identity)) 42)
   ((selfie (selfie (selfie identity))) 42)) ; ad-infinitum

(= [\4 \2]
   (vec (str (inc 41)))
   ((comp vec str inc) 41))

;; EXERCISE
;; Use `(complement string?)` to make the following true
;; - `complement` accepts a "predicate" function, and returns a
;;   function that does the opposite of the given "predicate"

(defn my-complement [arg] (complement arg) )

(= (not (string? "hi"))
   (my-complement "hi"))

(comment
  ;; "Predicate" is just a term we use to conveniently describe
  ;; any function that returns a truthy/falsey value, i.e.
  ;; any function that is used to test for some condition.
  ;; These so-called "predicates" are not inherently special.
  )

(def x 42)      ; Bind `x` to 42, globally ("top-level" binding)

(identity x)    ; obviously returns 42

((fn [x] x)  x) ; also returns 42, but how?

(let [x 10]     ; We use `let` to bind things locally.
  x)            ; This evaluates to the value of the "let-bound" `x`.

;; (+ (let [x 10]
     x)
   x)           ; So, this whole thing should evaluate to what?


;; EXERCISE
;; Read carefully, and compare these three function variants:

(defn add-one-v1
  [x]
  (+ x 1)) ; which `x` will this `x` reference?

(add-one-v1  1) ; should evaluate to what?
(add-one-v1  x) ; should evaluate to what?


(defn add-one-v2
  [z]
  (+ x 1)) ; which `x` will this `x` reference?

(add-one-v2  1) ; should evaluate to what?
(add-one-v2  x) ; should evaluate to what?


(defn add-one-v3
  [x]
  (let [x 10]
    (+ x 1))) ; which `x` will this `x` reference?

(add-one-v3  1) ; should evaluate to what?
(add-one-v3  x) ;
                ; 


(def PI 3.141592653589793)

(defn scale-by-PI
  [n]
  (* n PI)) ; PI is captured within the body of `scale-by-PI`

(scale-by-PI 10)


;; A more general way to "scale by":
;; - Thanks to lexical scope + the function closure property

(defn scale-by
  "Given a number `x`, return a function that accepts
  another number `y`, and scales `y` by `x`."
  [x]
  (fn [y] (* y x))) ; whatever is passed as `x` is captured
                    ; within the body of the returned function



(comment
  ;; BONUS EXERCISES
  ;; Define a few scaling functions, in terms of `scale-by`
  ;;
  (def scale-by-PI-v2
    (fn [y] (* y)))

  (def quadruple
    "4x the given number."
    (fn [y] (* y 4)))
  
  (quadruple 10)
  (def halve
    (fn [x] (* x 0.5)))
  )
(halve 10)

(map true? [1 true 3 false])
(filter #(= 0 (rem % 2)) [1 2 3 4 5])
(reduce + 10 [1 2 3 4 5])

f :: acc -> i -> acc
(+ 10 1)
(+ (+ 10 1) 2)
(+ (+ (+ 10 1) 2) 3)
(+ (+ (+ (+ 10 1)) 3) 4)
(+ (+ (+ (+ (+ 10 1)) 3) 4) 5)


(defn my-map [f coll]
  (reduce (fn [accum, currentVal] (conj accum (f currentVal)))[] coll))

(my-map inc [1 2 3 4])

(if false (+ 1 2 3))

(if false 
  (println "A") ˀ
  (println "B"))

(when true (+ 1 2 3))

((
  (fn [x] (x x))
  (fn [x] x)
  ) '10)

(((fn [f] (f f)) identity) 10)

(identity identity)

;;((fn [x] (x x)) (fn [x] (x x))) ;; 4eva
                              
;-----------

(def earth-alt
  {:pname "Earth"
   :mass 1
   :radius 1
   :moons 1
   :atmosphere {:nitrogen 78.08
                :oxygen 20.95
                :carbon-dioxide 0.4
                :water-vapour 0.10
                :argon 0.33
                :traces 0.14}})

;; EXERCISE
;; `get` and `get-in` work as expected
;; - Use `get` to extract :traces from `earth-alt`'s atmosphere
;; - The use `get-in` to do the same

(get (get earth-alt :atmosphere) :traces)

(get-in earth-alt [:atmosphere :traces]) 



;; BUT, unlike plain old strings, keywords also behave as
;; _functions_ of hash-maps, and can look themselves up
;; in any given hash-map.

;; ("pname" earth)  ; Will FAIL!

(:pname earth-alt) ; Works!


;; EXERCISE
;; Extract `:argon` from the `:atmosphere` of `earth-alt`

(:argon (:atmosphere earth-alt))

(def planets
  [{:pname "Mercury" :moons 0  :mass 0.0533}
   {:pname "Venus"   :moons 0  :mass 0.815}
   {:pname "Earth"   :moons 1  :mass 1}
   {:pname "Mars"    :moons 2  :mass 0.107}])

(map(fn [planet] (:pname planet)) planets)

(map :pname planets)

;; EXERCISE
;; `filter` out planets with less `:mass` than the Earth

(defn less-mass-than-earth?
  [planet]
  (< (:mass planet) 1))

(filter less-mass-than-earth? planets)




;; EXERCISE
;; Recall how to use `filter`, `map`, and `reduce`:
(filter even? [1 2 3 4])
(map    inc   [1 2 3 4])
(reduce + 0   [1 2 3 4])
;; Use these to compute the total `:mass` of planets
;; having less mass than the Earth.



;; Maps, Vectors, and Sets also behave like functions!
;; - We don't normally use maps and vector in the function
;;   position to perform lookups (there are a few problems
;;   with doing so), but we often use _well-defined_ sets as
;;   predicate functions, to test for set membership.

;; Maps can "self-look-up" keys

({:a "a", :b "b"} :a)

;; Vectors can "self-look-up" by index position

(["a" "b" "c"] 0)

;; Sets can self-test set membership

(#{"a" "b" "c"} "b")   ; truthy: return set member if it exists
(#{"a" "b" "c"} "boo") ; falsey: return `nil` if it doesn't

;; Lists do NOT behave like functions

#_('("a" "b" "c") 0)   ; FAIL

;; EXERCISE
;; Define a predicate `poison-gas?` which returns the
;; poison gas if it belongs to a set of known poison gases,
;; or `nil` (falsey) otherwise. These are some known poison gases:
:carbon-monoxide, :chlorine, :helium
:sulphur-dioxide, :hydrogen-chloride


(defn poison-gas?
  [gas]
  "Does the given gas belong to a set of known poison gases?"
  (boolean (#{:carbon-monoxide, :chlorine, :helium
              :sulphur-dioxide, :hydrogen-chloride} gas)))

(poison-gas? :chlorine)                 ; truthy
(poison-gas? :oxygen)                   ; falsey


