(ns winter-onboarding-2021.4clojure.alisha)

(defn problem4 [] (list :a :b :c))

(defn problem5 [] (conj '(2 3 4) 1))

(defn problem6 [] (list :a :b :c) (vec '(:a :b :c)) (vector :a :b :c))

(defn problem7 [] (conj [1 2] 3 4))

(defn problem8 [] (set '(:a :a :b :c :c :c :c :d :d)))

(defn problem9 [] (conj #{1 4 3} 2))

(defn problem10_1 [] ((hash-map :a 10, :b 20, :c 30) :b))

(defn problem10_2 [] (:b {:a 10, :b 20, :c 30}))


