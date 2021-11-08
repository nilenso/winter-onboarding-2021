(ns winter-onboarding-2021.dice-roller.yogi.core
  (:require [winter-onboarding-2021.dice-roller.yogi.models :as models]
            [winter-onboarding-2021.dice-roller.yogi.operations :as operations]))

(def x (models/build-dice-model 6 5 :keep {:selector-type :equal-to :value 2}))

x

(defn init [dice-model]
  (assoc dice-model 
   :states 
   (conj (dice-model :states) 
         (take (dice-model :num-dice) 
               (repeatedly #(+ 1
                               (rand-int (dice-model :num-faces))))))))

(defn apply-operation
  [dice-roller]
  (case (dice-roller :operation)
    :keep (operations/my-keep dice-roller)
    :drop (operations/my-drop dice-roller)
    :reroll (operations/reroll dice-roller)))

(operations/reroll (init x))

(defn eval-dice-model [dice-model]
  (apply-operation (init dice-model)))

(eval-dice-model x)