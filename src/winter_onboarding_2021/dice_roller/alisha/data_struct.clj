(ns winter-onboarding-2021.dice-roller.alisha.data_struct)

;;Build data structure for dice-roll (representation of dice-notation)
(defn build-dice-input [num-faces num-rolls]
  {:num-faces num-faces
   :num-rolls num-rolls})

;; Build dice-result
(defn build-dice-result [rolls]
  {:type :dice-result
   :rolls rolls})

;;Build data structure for result, which contains input infor and outcomes
;;output -> sequence of dice-roll-outcomes
(defn build-dice-struct [{:keys [num-faces num-rolls]}]
  {:type :dice-struct
   :num-faces num-faces
   :num-rolls num-rolls})

;;A single dice-roll is represented as dice-roll-outcome
(defn build-dice-roll-outcome [value]
  {:type :dice-roll-outcome
   :value value})

(defn build-operation [op selector]
  {:op op
   :selector selector})

(defn build-selector [op x]
  {:op op
   :x x})

(defn build-bin-op [op left right]
  {:type :bin-op
   :op op
   :left left
   :right right})

(defn build-numberic-struct [num]
  {:type :numeric
   :value num})
