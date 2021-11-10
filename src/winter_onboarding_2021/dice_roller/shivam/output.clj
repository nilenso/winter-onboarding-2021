(ns winter-onboarding-2021.dice-roller.shivam.output
  (:require [clojure.string :as string]
            [winter-onboarding-2021.dice-roller.shivam.models :as models]))

(declare stringify)

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
  (let [stringified-values (map stringify set-values)]
    (if (= 0 (count stringified-values))
      ""
      (format "(%s)"
              (string/join ", " stringified-values)))))

(defn join-value-and-previous-values [value previous-values-string]
  {:pre [(or (number? value) (string? value))
         (string? previous-values-string)]
   :post [(string? %)]}
  (format "%s%s" value previous-values-string))

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
      (add-discard-indicators (join-value-and-previous-values
                               value
                               previous-values-string))
      (join-value-and-previous-values value previous-values-string))))

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

(defn stringify-unary-op [unary-op]
  {:pre [(= (:type unary-op) :evaluated-unary-op)]}
  nil)

(defn stringify-bin-op [bin-op]
  {:pre [(= (:type bin-op) :evaluated-bin-op)]}
  nil)

(defn stringify-set [st]
  {:pre [(= (:type st) :evaluated-set)]}
  (let [{:keys [value values operation]} st]
    (println operation)
    (format "%s%s => %s"
            (stringify-set-values values)
            (stringify-operation operation)
            value)))

(defn stringify-dice [dice]
  {:pre [(= (:type dice) :evaluated-dice)]}
  (let [{:keys [num-rolls num-faces operation values value]} dice
        stringified-die-values (format "(%s)" (string/join ", " (map stringify-die values)))]
    (format "%sd%s%s: %s => %s"
            num-rolls
            num-faces
            (stringify-operation operation)
            stringified-die-values
            value)))

(defn stringify [entity]
  (if (string? entity)
    entity
    (case (:type entity)
      :evaluated-set (stringify-set entity)
      :evaluated-bin-op (stringify-bin-op entity)
      :evaluated-unary-op (stringify-unary-op entity)
      :die (stringify-die entity)
      :evaluated-dice (stringify-dice entity)
      :literal (stringify-literal entity))))
