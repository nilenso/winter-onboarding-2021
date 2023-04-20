(ns winter-onboarding-2021.dice-roller.yogi.selectors)


(defn highest [x list] (take x (reverse (sort (vec list)))))

(defn lowest [x list] (take x  (sort (vec list))))


(defn greater [x list]
  (filter
   (fn [y] (< x y))
   list))

(defn lower [x list]
  (filter
   (fn [y] (> x y))
   list))

(defn equal [x list]
  (filter
   (fn [y] (= x y))
   list))