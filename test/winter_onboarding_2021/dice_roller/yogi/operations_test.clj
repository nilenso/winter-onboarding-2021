(ns winter-onboarding-2021.dice-roller.yogi.operations-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.yogi.operations :as operations]))

;dice-models

(deftest keep-test
  (let [dice-1 {:num-faces 6
                :num-dice 5
                :operation :keep
                :selector {:selector-type :lower
                           :value 4}
                :states [nil '(1 2 4 3 2)]}]
    (testing "if kept elements are appended to the states vector"
      (is (= (:states (operations/my-keep dice-1))
             [nil '(1 2 4 3 2) '(1 2 3 2)]))))

  (let [dice-3 {:num-faces 6
                :num-dice 5
                :operation :keep
                :selector {:selector-type :equal-to
                           :value 2}
                :states [nil]}]
    (testing "if size of states is greater than original"
      (is (>= (count (:states (operations/my-keep dice-3)))
              (count (:states dice-3))))))

  (let [dice-3 {:num-faces 6
                :num-dice 5
                :operation :keep
                :selector {:selector-type :equal-to
                           :value 2}
                :states [nil]}]
    (testing "if size of last element of states is less than or equal to previous"
      (is (<= (count (last (:states (operations/my-keep dice-3))))
              (count (second (reverse (operations/my-keep dice-3)))))))))
;; => #'winter-onboarding-2021.dice-roller.yogi.operations-test/keep-test

(deftest drop-test
  (let [dice-1 {:num-faces 6
                :num-dice 5
                :operation :drop
                :selector {:selector-type :lower
                           :value 4}
                :states [nil '(1 2 4 3 2)]}]
    (testing "if dropped elements are appended to the states vector"
      (is (= (:states (operations/my-drop dice-1))
             [nil '(1 2 4 3 2) '(4)]))))

  (let [dice-3 {:num-faces 6
                :num-dice 5
                :operation :drop
                :selector {:selector-type :equal-to
                           :value 2}
                :states [nil]}]
    (testing "if size of states is greater than original"
      (is (>= (count (:states (operations/my-drop dice-3)))
              (count (:states dice-3))))))

  (let [dice-3 {:num-faces 6
                :num-dice 5
                :operation :drop
                :selector {:selector-type :equal-to
                           :value 2}
                :states [nil]}]
    (testing "if size of last element of states is less than or equal to previous"
      (is (<= (count (last (:states (operations/my-drop dice-3))))
              (count (second (reverse (operations/my-drop dice-3)))))))))

(deftest reroll-test

  (testing "reroll until condition met"
    (let [dice-1 {:num-faces 6
                  :num-dice 5
                  :operation :reroll
                  :selector {:selector-type :lower
                             :value 3}
                  :states [nil '(1 4 2 3 1)]}]
      (with-redefs [rand-int (fn [_]
                               (- (dice-1 :num-faces) 1))]
        (is (=  (last (:states (operations/reroll dice-1)))
                '(6 4 6 3 6)))))
    
    (testing "number of elements of non-first states are equal"
      (let [dice-1 {:num-faces 6
                    :num-dice 6
                    :operation :reroll
                    :selector {:selector-type :lower
                               :value 4}
                    :states [nil '(1 4 2 3 1 3)]}]
        (with-redefs [rand-int (fn [_]
                                 (- (:num-faces dice-1) 1))]
          (is (= (count (set (rest (map count ((operations/reroll dice-1) :states)))))
                 1)))))))

