(ns winter-onboarding-2021.dice-roller.alisha.operations
  (:require [winter-onboarding-2021.dice-roller.alisha.selector :as selector])
  (:require [winter-onboarding-2021.dice-roller.alisha.utils :as utils]))

(defn keep [selector rolls]
  (let [rolls-after-selector-op (selector/selector-op selector rolls)]
    (map selector/discard-if-not-selected rolls-after-selector-op)))

#_(keep {:op :highest :x 2} rolls)

(defn drop [selector rolls]
  (let [rolls-after-selector-op (selector/selector-op selector rolls)]
    (map selector/discard-if-selected rolls-after-selector-op)))

#_(drop {:op :lowest :x 2} rolls)

(defn reroll [selector rolls reroll-count num-faces]
  (let [rolls-after-selector-op (selector/selector-op selector rolls)
        append-previous-values #(if (:selected %)
                                  (let [prev-roll-val (:value %)
                                        prev-vals (:previous-values %)
                                        outcome-with-prev-vals (if (not= prev-vals nil)
                                                                 (assoc % :previous-values (conj prev-vals prev-roll-val))
                                                                 (assoc % :previous-values (conj () prev-roll-val)))]
                                    (assoc outcome-with-prev-vals :value (utils/roll num-faces)))
                                  %)]
    (cond
      (> reroll-count 100) (throw (Exception. "Reached maximum rerolls"))
      (selector/none-selected? rolls-after-selector-op) rolls-after-selector-op
      :else
      (reroll selector (map append-previous-values rolls-after-selector-op) (inc reroll-count) num-faces))))

#_(reroll {:op :> :x 2} rolls 0 4)

(defn operate [{:keys [operation num-faces]} rolls]
  (let [op (:op operation)
        selector (:selector operation)
        oped-rolls (case op
                     :keep (keep selector rolls)
                     :drop (drop selector rolls)
                     :reroll (reroll selector rolls 0 num-faces))]
    (selector/dissoc-selected-from-rolls oped-rolls)))

#_(operate {:op :keep :selector {:op :highest :x 2}} rolls 4)
