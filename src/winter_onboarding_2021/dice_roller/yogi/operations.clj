(ns winter-onboarding-2021.dice-roller.yogi.operations)

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

(defn my-keep
  ([selector x seq]
   (filter (partial check (selector x seq)) seq))
  ([y seq]
   (filter (partial check y) seq)))

(defn drop 
  ([selector x seq]
   (filter (partial rcheck (selector x seq)) seq))
  ([y seq]
   (filter (partial rcheck y) seq)))

(defn solve [list]
  (eval (conj (eval list) +)))


(defn reroll [theexp]
  theexp)


