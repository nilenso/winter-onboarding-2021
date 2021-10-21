(ns winter-onboarding-2021.dice-roller.yogi.functions)

(defn my-roller [x y]
  (repeatedly x #(+ 1 (rand-int y))))



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

(defn h [x list] (take x (reverse (sort (vec list)))))

(defn l [x list] (take x  (sort (vec list))))

(defn g [x list] (reverse (reduce (fn [y z] (if (> x (- z 1)) () (conj y z))) (sort (vec list)))))

(defn lo [x list]  (reduce (fn [y z] (if (> x z) (conj y z) ())) (reverse (sort (vec list)))))

;(lo 4  [ -1 4 1 2 3 4 1 5])

(h 2 '(1 2 3 54 1 47 12))