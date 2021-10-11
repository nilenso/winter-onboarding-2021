(ns winter-onboarding-2021.4clojure.shivam)

;; define a function my-map which should take a function and collection 

;; (defn my-map [f coll]
;;   (reduce (fn [accum, currentVal] (conj accum (f currentVal))) [] coll))

;; [1 2 3 4]
;; After 1st iteration: accum is [2], currentVal 1
;; After 2nd iteration: accum is [2 3], currentVal 2
;; (my-map inc [1 2 3 4])

;; (def earth
;;   {"pname" "Earth"
;;    "mass"   1
;;    "radius" 1
;;    "moons"  1
;;    "atmosphere" {"nitrogen"       78.08
;;                  "oxygen"         20.95
;;                  "carbon-dioxide" 0.4
;;                  "water-vapour"   0.10
;;                  "argon"          0.33
;;                  "traces"         0.14}})

;; (def earth-alt
;;   {:pname "Earth"
;;    :mass 1
;;    :radius 1
;;    :moons 1
;;    :atmosphere {:nitrogen 78.08
;;                 :oxygen 20.95
;;                 :carbon-dioxide 0.4
;;                 :water-vapour 0.10
;;                 :argon 0.33
;;                 :traces 0.14}})


;; (:argon (:atmosphere earth-alt))

;; (def planets
;;   [{:pname "Mercury" :moons 0  :mass 0.0533}
;;    {:pname "Venus"   :moons 0  :mass 0.815}
;;    {:pname "Earth"   :moons 1  :mass 1}
;;    {:pname "Mars"    :moons 2  :mass 0.107}])

;; (map :pname planets)

;; ;; EXERCISE
;; ;; `filter` out planets with less `:mass` than the Earth

;; (defn less-mass-than-earth?
;;   [planet]
;;   (< (:mass planet) 1))




;; (def planetsSmallerThanEarth (map :mass (filter less-mass-than-earth? planets))) ;; (0.52 0.8)

;; (reduce + 0 planetsSmallerThanEarth)




;; EXERCISE
;; Define a predicate `poison-gas?` which returns the
;; poison gas if it belongs to a set of known poison gases,
;; or `nil` (falsey) otherwise. These are some known poison gases:


;; (defn poison-gas?
;;   [gas]
;;   ;"Does the given gas belong to a set of known poison gases?"
;;   (boolean (#{:carbon-monoxide, :chlorine, :helium
;;     :sulphur-dioxide, :hydrogen-chloride} gas))
;;   )


;; (poison-gas? :chlorine)                 ; truthy
;; (poison-gas? :oxygen)                   ; falsey

; I am loving clojure





;; (map (fn [f] (f 42))
;;      [str identity inc dec (fn [x] x)])

;; ;; A data table:
;; [[:name :age :country]
;;  ["Foo" 10   "India"]
;;  ["Bar" 21   "Australia"]
;;  ["Baz" 18   "Turkey"]
;;  ["Qux" 42   "Chile"]]


;; (= '(1 2 3 4) (conj '(2 3 4) 1))

(defn problem5 [] '(1 2 3 4))

(defn problem6 [] [:a :b :c])

(defn problem7 [] [1 2 3 4])

(defn problem8 [] #{:a :b :c :d})

(defn problem9 [] 2)

(defn problem10 [] 20)

(defn problem11 [] {:b 2})

(defn problem12 [] 3)

(defn problem13 [] '(20 30 40))

(defn problem14 [] 8)

(defn problem15 [x] (* x 2))

(defn problem16 [name] (str "Hello, " name "!"))

(defn problem17 [] '(6 7 8))

(defn problem18 [] '(6 7))

(defn problem19 [] last)

(defn problem20 [coll] (second (reverse coll)))