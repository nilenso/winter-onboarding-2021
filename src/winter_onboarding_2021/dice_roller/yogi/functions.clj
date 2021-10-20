(ns winter-onboarding-2021.dice-roller.yogi.functions)

(defn my-roller [x y] 
  (repeatedly x #(rand-int (+ y 1))))


(defn check [x list]
  (if (seq? x)
    (contains? (set x) list)
    (= x list)))

(defn rcheck [x list]
  (if (seq? x)
    (not (contains? (set x) list))
    (not (= x list))))


(defn k [x list] 
  (filter (partial check x) list))


(defn d [x list] 
  (filter (partial rcheck x) list))



(defn solve [list] 
  (eval (conj (eval list) +)))


(defn rr [theexp]
  theexp)