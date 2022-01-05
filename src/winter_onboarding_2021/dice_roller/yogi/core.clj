(ns winter-onboarding-2021.dice-roller.yogi.core
  (:require [winter-onboarding-2021.dice-roller.yogi.operations :as operations]))

(declare eval-expr)

(defn apply-operation [dice-roller]
  (case (dice-roller :operation)
    :keep (operations/my-keep dice-roller)
    :drop (operations/my-drop dice-roller)
    :reroll (operations/reroll dice-roller)
    dice-roller))

(defn get-dice-value [dice-model]
  (reduce + (last (dice-model :states))))

(defn eval-dice-model [dice-model]
  (let [partially-evald-expr (apply-operation dice-model)]
    (assoc partially-evald-expr
           :result
           (get-dice-value partially-evald-expr))))

(defn eval-set [set] set)

(def ^:private operator-fns
  {:plus +
   :minus -
   :multiply *
   :divide /})

(defn eval-bin-op [{:keys [left right operator]}]
  (let [op (operator-fns operator)]
    (assert op)
    (op (eval-expr left) (eval-expr right))))

(defn eval-expr [{:keys [type] :as expr}]
  (case type
    :dice-model (get-dice-value expr)
    :bin-op (eval-bin-op expr)
    :set (eval-set expr)
    :literal (expr :value)))

