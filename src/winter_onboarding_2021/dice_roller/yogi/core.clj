(ns winter-onboarding-2021.dice-roller.yogi.core
  (:require [winter-onboarding-2021.dice-roller.yogi.models :as models]))

(def x (models/build-dice-model 6 3 :keep {:type :equal-to :value 4}))

x

(defn roll-once [dice-model]
  (take (dice-model :num-dice) (repeatedly #(+ 1 (rand-int (dice-model :num-faces)))) ))


(defn apply-operation [dice-model]
  (dice-model :op))

(roll-once 6 3)
(defn eval-dice-model [dice-model]
  ((apply-operation (roll-once (dice-model :num-faces) (dice-model :num-dice)))))
