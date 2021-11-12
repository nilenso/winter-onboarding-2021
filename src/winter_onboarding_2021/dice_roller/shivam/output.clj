(ns winter-onboarding-2021.dice-roller.shivam.output
  (:require [clojure.string :as string]))

(declare stringify-by-type)
(declare flatten-by-type)

(def op-string-map
  {:keep "k"
   :drop "d"
   :reroll "rr"})

(def selector-string-map
  {:equals ""
   :lesser-than "<"
   :greater-than ">"
   :lowest "l"
   :highest "h"})

(defn add-discard-indicators [entity-string]
  {:pre [(string? entity-string)]}
  (format "~%s~" entity-string))

;; previous-values is a collection of Numbers
(defn stringify-previous-values [previous-values]
  (let [values-with-discard-indicators
        (map
         #(-> %
              str
              add-discard-indicators)
         previous-values)]
    (if (= 0 (count values-with-discard-indicators))
      ""
      (format " (%s)"
              (string/join ", " values-with-discard-indicators)))))

(defn stringify-set-values [set-values]
  (let [stringified-values (map stringify-by-type set-values)]
    (if (empty? stringified-values)
      ""
      (format "(%s)"
              (string/join ", " stringified-values)))))

(defn stringify-literal [{:keys [type] :as literal}]
  {:pre [(= type :literal)]}
  (let [{:keys [value discarded]} literal]
    (if discarded
      (add-discard-indicators (str value))
      (format "%s" value))))

(defn stringify-die [die]
  {:pre [(= (:type die) :die)]
   :post [(string? %)]}
  (let [{:keys [value discarded previous-values]} die
        previous-values-string (stringify-previous-values previous-values)]
    (if discarded
      (add-discard-indicators (str value previous-values-string))
      (str value previous-values-string))))

(defn stringify-selector [selector]
  {:pre [(= (:type selector) :set-selector)]}
  (let [{:keys [criteria num]} selector
        criteria-string (criteria selector-string-map)]
    (format "%s%s" criteria-string num)))

(defn stringify-operation [operation]
  (if (nil? operation)
    ""
    (let [{:keys [op selector]} operation
          operation-string (op op-string-map)
          selector-string (stringify-selector selector)]
      (format "%s%s" operation-string selector-string))))

(defn stringify-set [st]
  {:pre [(= (:type st) :evaluated-set)]}
  (let [{:keys [value values operation]} st]
    (format "%s%s: %s"
            (format "(%s)" (string/join ", " (map #(-> %
                                                       :value
                                                       str) values)))
            (stringify-operation operation)
            (stringify-set-values values))))

(defn stringify-dice [dice]
  {:pre [(= (:type dice) :evaluated-dice)]}
  (let [{:keys [num-rolls num-faces operation values value]} dice
        stringified-die-values (format "(%s)"
                                       (string/join ", " (map stringify-die values)))]
    (format "%sd%s%s: %s"
            num-rolls
            num-faces
            (stringify-operation operation)
            stringified-die-values)))

(defn stringify-by-type [entity]
  (if (string? entity)
    entity
    (case (:type entity)
      :evaluated-set (stringify-set entity)
      :evaluated-dice (stringify-dice entity)
      :literal (stringify-literal entity))))

(defn extract-sets-from-unary-op [accumulator unary-op]
  {:pre [(= (:type unary-op) :evaluated-unary-op)]}
  (flatten-by-type accumulator (:operand unary-op)))

(defn extract-sets-from-set [accumulator st]
  {:pre [(= (:type st) :evaluated-set)]}
  (let [{:keys [values]} st
        accumulated-with-self (conj accumulator st)
        accumulated-with-values (reduce (fn [accum, ele] (flatten-by-type accum ele))
                                        accumulated-with-self
                                        values)]
    accumulated-with-values))

(defn extract-sets-from-bin-op [accumulated bin-op]
  {:pre [(= (:type bin-op) :evaluated-bin-op)]}
  (let [{:keys [left right]} bin-op
        left-with-accumulated (flatten-by-type accumulated left)
        right-sets (flatten-by-type left-with-accumulated right)]
    right-sets))

(defn flatten-by-type [accumulated entity]
  (case (:type entity)
    :literal accumulated
    :die accumulated
    :evaluated-dice (conj accumulated entity) ;; conj because we are sure that entity is a hashmap & not a vector
    :evaluated-bin-op (extract-sets-from-bin-op accumulated entity)
    :evaluated-set (extract-sets-from-set accumulated entity)
    :evaluated-unary-op (extract-sets-from-unary-op accumulated entity)))

(defn generate-strings [flattened]
  (map #(format "%s => %s"
                (stringify-by-type %)
                (:value %))
       flattened))

(defn append-total-to [generated-strings entity]
  (format "%s\\nTotal: %s"
          (string/join "\\n" generate-strings)
          (:value entity)))


