(ns winter-onboarding-2021.4clojure.shivam)

;; define a function my-map which should take a function and collection 

(defn my-map [f coll]
  (reduce (fn [accum, currentVal] (conj accum (f currentVal))) [] coll))

;; [1 2 3 4]
;; After 1st iteration: accum is [2], currentVal 1
;; After 2nd iteration: accum is [2 3], currentVal 2
(my-map inc [1 2 3 4])