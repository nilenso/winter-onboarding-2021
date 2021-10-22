(ns winter-onboarding-2021.4clojure.shivam
  (:require [clojure.set :as set]
            [clojure.string :as str]))

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

(defn problem20 [coll]
  (second (reverse coll)))

(defn problem21 [coll index]
  (if (>= index 0)
    ((vec coll) index)
    ((vec coll) (+ (count coll) index))))

(defn problem22 [seq-coll]
  (reduce
   (fn [acc _] (+ acc 1))
   0
   seq-coll))

(defn problem23 [coll]
  (reduce
   (fn [acc, ele] (cons ele acc))
   (empty coll)
   coll))

(defn problem24 [coll]
  (reduce
   (fn [acc ele] (+ acc ele))
   0
   coll))

(defn problem25 [coll]
  (filter
   (fn [x] (not (= 0 (rem x 2))))
   coll))

(defn problem26 [n]
  (reduce
   (fn [acc, ele]
     (if (<= n 1)
       (vec (range n))
       (conj acc (+ (or (second (reverse acc)) 1) (last acc)))))
   (if (<= n 1)
     []
     [0])
   (range (- n 1))))

(defn problem27 [coll]
  (if (= java.lang.String (type coll))
    (= (apply str (reverse coll)) coll)
    (= (reverse coll) coll)))


(defn problem28 [coll]
  (reduce
   (fn [acc, ele]
     (concat
      acc
      (if (coll? ele)
        (problem28 ele)
        [ele])))
   []
   coll))

(defn problem29 [test-str]
  (apply
   str
   (filter
    (fn [c] (Character/isUpperCase c))
    test-str)))

(defn problem30 [to-compress-seq]
  (if (< (count to-compress-seq) 2)
    to-compress-seq
    (if (= (first to-compress-seq) (second to-compress-seq))
      (problem30 (rest to-compress-seq))
      (conj (problem30 (rest to-compress-seq)) (first to-compress-seq)))))

(defn problem32 [to-duplicate-seq]
  (reduce
   (fn [acc, ele] (conj acc ele ele))
   (empty to-duplicate-seq)
   to-duplicate-seq))

;; `n` is the number of seqs in the resultant seq
;; `iterations` is the number of elements in each seq
;; `accum` is the resultant seq
(defn problem43 [my-seq n]
  (let [iterations (/ (count my-seq) n) accum []]
    (conj (for [i (range iterations)]
            (for [j (range n)]
              ((vec my-seq) ((* i iterations) + j)))))))

;; instead of building all child seqs together,
;; build one child seq at one time and then conj that child seq to parent seq `accum`
;; my-seq: the flattened seq

(defn problem43'
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
  (= num (some
          (fn [elem]
            (when (<= num (* elem elem))
              (* elem elem)))
          (range (inc (quot num 2))))))

(defn problem74 [string]
  (let [nums-vec (str/split string #",")]
    (->>
     (filter (comp is-perfect-square #(Integer/parseInt %)) nums-vec)
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