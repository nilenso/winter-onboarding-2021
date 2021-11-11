(ns winter-onboarding-2021.dice-roller.alisha.core
  (:require [winter-onboarding-2021.dice-roller.alisha.data_struct :as data-struct])
  (:require [winter-onboarding-2021.dice-roller.alisha.utils :as utils]))

(defn roll [num-faces] (inc (rand-int num-faces)))

;;generate rolls
(defn generate-rolls [dice-struct]
  (let [num-rolls (:num-rolls dice-struct)
        num-faces (:num-faces dice-struct)
        rolls (map #(data-struct/build-dice-roll-outcome %)
                   (repeatedly num-rolls (roll num-faces)))]
    (assoc dice-struct :rolls rolls)))

(defn equals [x roll-outcome]
  (= (:value roll-outcome) x))

(defn lesser-than [x roll-outcome]
  (< (:value roll-outcome) x))

(defn greater-than [x roll-outcome]
  (> (:value roll-outcome) x))

(defn highest-x [x rolls]
  (take x (reverse (sort-by :value rolls))))

(defn lowest-x [x rolls]
  (take x (sort-by :value rolls)))

(defn selector-op [{:keys [op x]}
                   rolls]
  (let [wrapper-selector #(partial utils/append-selected %)]
    (case op
      :> (map (wrapper-selector (partial greater-than x)) rolls)
      :< (map (wrapper-selector (partial lesser-than x)) rolls)
      := (map (wrapper-selector (partial equals x)) rolls)
      :highest (utils/mark-rolls-selected (highest-x x rolls) (utils/mark-rolls-unselected rolls))
      :lowest (utils/mark-rolls-selected (lowest-x x rolls) (utils/mark-rolls-unselected rolls)))))

(defn keep [selector rolls]
  (let [rolls-after-selector-op (selector-op selector rolls)]
    (map utils/discard-if-not-selected rolls-after-selector-op)))

#_(keep {:op :highest :x 2} rolls)

(defn drop [selector rolls]
  (let [rolls-after-selector-op (selector-op selector rolls)]
    (map utils/discard-if-selected rolls-after-selector-op)))

#_(drop {:op :lowest :x 2} rolls)

(defn reroll [selector rolls reroll-count num-faces]
  (let [rolls-after-selector-op (selector-op selector rolls)
        append-previous-values #(if (:selected %)
                   (let [prev-roll-val (:value %)
                         prev-vals (:previous-values %)
                         outcome-with-prev-vals (if (not= prev-vals nil)
                                                  (assoc % :previous-values (conj prev-vals prev-roll-val))
                                                  (assoc % :previous-values (conj () prev-roll-val)))]
                     (assoc outcome-with-prev-vals :value (roll num-faces)))
                   %)]
    (cond
      (> reroll-count 100) (throw (Exception. "Reached maximum rerolls"))
      (utils/none-selected? rolls-after-selector-op) rolls-after-selector-op
      :else
      (reroll selector (map append-previous-values rolls-after-selector-op) (inc reroll-count) num-faces))))

#_(reroll {:op :> :x 2} rolls 0 4)

(defn operation [operation rolls num-faces]
  (let [op (:op operation)
        selector (:selector operation)]
    (case op
      :keep (keep selector rolls)
      :drop (drop selector rolls)
      :reroll (reroll selector rolls 0 num-faces))))

#_(operation {:op :keep :selector {:op :highest :x 2}} rolls 4)
