(ns winter-onboarding-2021.dice-roller.alisha.utils)

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

(defn discard-if-not-selected [roll]
   (if (:selected roll)
       (assoc roll :discarded false)
       (assoc roll :discarded true)))
