(ns winter-onboarding-2021.dice-roller.yogi.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.yogi.core :as core]
            [winter-onboarding-2021.dice-roller.yogi.models :as models]))

(deftest eval-dice-model-test
  (testing "evalutes the dice model"
    (let [left (models/build-dice-model 6 6 :reroll {:selector-type :lower :value 3})]
      (with-redefs [rand-int (fn [x] (- x 1))]
        (is (= {:type :dice-model
                :num-faces 6
                :num-dice 6
                :operation :reroll
                :selector {:selector-type :lower
                           :value 3}
                :states [nil '(6 6 6 6 6 6)]
                :result 36}
               (core/eval-dice-model left)))))))


(deftest eval-bin-op-test
  (testing "operates on two dice-models"
    (let [left (models/build-dice-model 6 6 :reroll {:selector-type :lower :value 4})
          right (models/build-dice-model 6 6 :keep {:selector-type :lower :value 3})
          operator :plus]
      (with-redefs [rand-int (fn [x] (- x 1))]
        (is (= 72
               (core/eval-bin-op {:left left
                                  :right right
                                  :operator operator}))))))
  
  (testing "operates on one dice-model and one bin-op"
    (let [left (models/build-dice-model 6 6 :reroll {:selector-type :lower :value 4})
          bin-op (models/build-bin-op (models/build-dice-model 6 6 :keep {:selector-type :lower :value 3}) left :plus)
          operator :plus]
      (with-redefs [rand-int (fn [x] (- x 1))]
        (is (= 108
               (core/eval-bin-op {:left left
                                  :right bin-op
                                  :operator operator})))))))

