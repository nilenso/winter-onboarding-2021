(ns winter-onboarding-2021.dice-roller.shivam.utils)

(def valid-selectors ["" "<", ">", "l", "h"])

(def valid-ops ["k" "d" "rr"])

;; Generates a random Int from set (0 upper-limit]
(defn gen-rand-int [upper-limit]
  (inc (rand-int upper-limit)))