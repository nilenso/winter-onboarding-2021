(ns winter-onboarding-2021.dice-roller.alisha.core
  (:require [winter-onboarding-2021.dice-roller.alisha.data_struct :as data-struct]))

(defn roll [num-faces] #(inc (rand-int num-faces)))

;;generate rolls
(defn generate-rolls [{:keys [num-rolls num-faces]}]
  (map #(data-struct/build-dice-roll-outcome %)
       (repeatedly num-rolls (roll num-faces))))
