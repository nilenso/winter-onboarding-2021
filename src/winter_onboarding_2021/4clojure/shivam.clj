(ns winter-onboarding-2021.4clojure.shivam
  (:require [clojure.set :as set]
            [clojure.string :as str]))

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

(defn problem21 [coll index] (if (>= index 0)
                               ((vec coll) index)
                               ((vec coll) (+ (count coll) index))))

(defn problem22 [seq-coll] (reduce (fn [acc _] (+ acc 1)) 0 seq-coll))

(defn problem23 [coll] (reduce
                        (fn [acc, ele] (cons ele acc))
                        (empty coll)
                        coll))

(defn problem24 [coll] (reduce
                        (fn [acc ele] (+ acc ele))
                        0
                        coll))

(defn problem25 [coll] (filter
                        (fn [x] (not (= 0 (rem x 2))))
                        coll))

(defn problem26 [n] (reduce
                     (fn [acc, ele]
                       (if (<= n 1)
                         (vec (range n))
                         (conj acc (+ (or (second (reverse acc)) 1) (last acc)))))
                     (if (<= n 1)
                       []
                       [0])
                     (range (- n 1))))

(defn problem27 [coll] (if (= java.lang.String (type coll))
                         (= (apply str (reverse coll)) coll)
                         (= (reverse coll) coll)))


(defn problem28 [coll] (reduce
                        (fn [acc, ele] (concat acc (if (coll? ele)
                                                     (problem28 ele)
                                                     [ele]))) [] coll))

(defn problem29 [test-str]
  (apply str
         (filter
          (fn [c] (Character/isUpperCase c))
          test-str)))

(defn problem30 [to-compress-seq] (if (< (count to-compress-seq) 2)
                                    to-compress-seq
                                    (if (= (first to-compress-seq) (second to-compress-seq))
                                      (problem30 (rest to-compress-seq))
                                      (conj (problem30 (rest to-compress-seq)) (first to-compress-seq)))))

(defn problem32 [to-duplicate-seq] (reduce (fn [acc, ele] (conj acc ele ele)) (empty to-duplicate-seq) to-duplicate-seq))




;; `n` is the number of seqs in the resultant seq
;; `iterations` is the number of elements in each seq
;; `accum` is the resultant seq
(defn problem43 [my-seq n] (let [iterations (/ (count my-seq) n) accum []]
                             (conj (for [i (range iterations)]
                                     (for [j (range n)]
                                       ((vec my-seq) ((* i iterations) + j)))))))



;; instead of building all child seqs together,
;; build one child seq at one time and then conj that child seq to parent seq `accum`
;; my-seq: the flattened seq

(defn problem43
  [flattened-seq num-child]
  ;; `n`is the number of child seqs & `iterations` is the number of elemnents in each child seq
  (let [desearlize (fn [acc, [index values]] (assoc acc index (map second values)))]
    (reduce desearlize [] (group-by first (for [i (range (count flattened-seq))]
                                            (let [seq-index (rem i num-child)]
                                              [seq-index (nth flattened-seq i)]))))))





;; (defn differs-by-only-1-letter? [word1 word2]
;;   (let [letter-diff (if (>= (count word1) (count word2))
;;                       (set/difference (set word1) (set word2))
;;                       (set/difference (set word2) (set word1)))]
;;     (= 1 (count letter-diff))))


;; (defn find-differ-by-1-letter [current-word coll]
;;   (filter (fn [word]
;;             (differs-by-only-1-letter? word current-word))
;;         coll))

;; (defn enumerate-differ-by-1-words [coll]
;;   (into {} (map (fn [word] [word (find-differ-by-1-letter word coll)]) coll)))


;; (defn problem82? [coll current-word]
;;   (if (empty? coll)
;;     true
;;     (let [next-word (find-differ-by-1-letter current-word coll)]
;;       (if next-word
;;         (problem82? (remove #{next-word} coll) next-word)
;;         false))))



;; 15 : 15/2 = 7.5
(defn is-perfect-square [num]
  (= num (some (fn [elem] (when (<= num (* elem elem))
                            (* elem elem)))
               (range (inc (quot num 2))))))

(defn problem74 [string]
  (let [nums-vec (str/split string #",")]
    (->> (filter (comp is-perfect-square #(Integer/parseInt %)) nums-vec)
         (str/join ","))))

;; (defn find-divisors [num]
;;   (filter
;;    (fn [ele] (= 0 (rem num ele)))
;;    (range 1 (inc (quot num 2)))))

;; (defn are-co-prime [num1 num2]
;;   (let [
;;         num1divisors (find-divisors num1)
;;         num2divisors (find-divisors num2)
;;         intersection (set/intersection (set num1divisors) (set num2divisors))]
;;     (when (= #{1} intersection)
;;       true)
;;     ))

;; (defn problem75 [num]
;;   (filter
;;    #(are-co-prime num %)
;;    (range 1 num)))

(defn problem102 [kebab-string]
  (let [[first-ele & rest-elems] (str/split kebab-string #"-")]
    (apply str first-ele (map str/capitalize rest-elems))))