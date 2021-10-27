(ns winter-onboarding-2021.dice-roller.shivam.utils)

(def valid-selectors [:equals
                      :lesser-than
                      :greater-than
                      :lowest
                      :highest])

(def valid-ops [:keep
                :drop
                :reroll])

;; Generates a random Int from set (0 upper-limit]
(defn gen-rand-int [upper-limit]
  (inc (rand-int upper-limit)))

;; Removes the first occurence of element in `coll` when (pred? element) returns true
(defn remove-once [pred? coll]
  ((fn inner [coll]
     (lazy-seq
      (when-let [[x & xs] (seq coll)]
        (if (pred? x)
          xs
          (cons x (inner xs))))))
   coll))

;; TODO: Using utils is a code smell.
;; need to shift these functions to their right places