(ns winter-onboarding-2021.dice-roller.alisha.selector)

(defn set-selected-false [dice-outcome]
  (assoc dice-outcome :selected false))

(defn set-selected-true [dice-outcome]
  (assoc dice-outcome :selected true))

(defn mark-rolls-unselected [rolls]
  (map set-selected-false rolls))

(defn append-selected [selector-fn roll-outcome]
  (if (selector-fn roll-outcome)
    (set-selected-true roll-outcome)
    (set-selected-false roll-outcome)))

(defn equals-roll-and-not-selected [selector-operated-roll
                                    roll]
  (and (= (:value selector-operated-roll) (:value roll))
       (not (:selected roll))))

(defn discard-if-not-selected [roll]
  (if (:selected roll)
    (assoc roll :discarded false)
    (assoc roll :discarded true)))

(defn discard-if-selected [roll]
  (if (:selected roll)
    (assoc roll :discarded true)
    (assoc roll :discarded false)))

(defn none-selected? [rolls]
  (let [selected-values (filter #(:selected %) rolls)]
    (empty? selected-values)))

(defn dissoc-selected-from-roll-outcome [roll]
  (dissoc roll :selected))

(defn dissoc-selected-from-rolls [rolls]
  (map dissoc-selected-from-roll-outcome rolls))

;;Update the roll if the roll matches the selector
(defn update-rolls [selector update-fn [roll & rest-rolls]]
  (if (selector roll)
    (conj rest-rolls (update-fn roll))
    (conj (update-rolls selector update-fn rest-rolls) roll)))

;;Update the rolls which matches selector-operated-roll by appending :selected true   
(defn mark-rolls-selected [[selector-operated-roll
                            & rest-selector-operated-roll] rolls]
  (if (some? selector-operated-roll)
    (mark-rolls-selected rest-selector-operated-roll (update-rolls
                                                      (partial equals-roll-and-not-selected selector-operated-roll)
                                                      set-selected-true
                                                      rolls))
    rolls))

;;selector functions--

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
  (let [wrapper-selector #(partial append-selected %)]
    (case op
      :> (map (wrapper-selector (partial greater-than x)) rolls)
      :< (map (wrapper-selector (partial lesser-than x)) rolls)
      := (map (wrapper-selector (partial equals x)) rolls)
      :highest (mark-rolls-selected (highest-x x rolls) (mark-rolls-unselected rolls))
      :lowest (mark-rolls-selected (lowest-x x rolls) (mark-rolls-unselected rolls)))))
