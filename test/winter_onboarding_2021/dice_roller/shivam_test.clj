(ns winter-onboarding-2021.dice-roller.shivam-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.shivam.utils :as utils]
            [winter-onboarding-2021.dice-roller.shivam.data-structs :as data-structs]
            [winter-onboarding-2021.dice-roller.shivam.core :as dice-roller]))

(def sample-selector {:type :set-selector
                      :criteria :lesser-than
                      :num 2})

(def sample-operation {:type :set-operation
                       :op :keep
                       :selector sample-selector})

(def sample-die {:type :die
                 :num-faces 5
                 :value 3})

(defn dies-factory [discard-sequence previous-values-sequence]
  [{:type :die, :num-faces 5, :value 3 :discarded (nth discard-sequence 0) :previous-values (nth previous-values-sequence 0)}
   {:type :die, :num-faces 5, :value 5 :discarded (nth discard-sequence 1) :previous-values (nth previous-values-sequence 1)}
   {:type :die, :num-faces 5, :value 3 :discarded (nth discard-sequence 2) :previous-values (nth previous-values-sequence 2)}
   {:type :die, :num-faces 5, :value 2 :discarded (nth discard-sequence 3) :previous-values (nth previous-values-sequence 3)}])

(def sample-values-for-die
  '({:type :die, :num-faces 5, :value 3 :discarded false :previous-values []}
    {:type :die, :num-faces 5, :value 5 :discarded false :previous-values []}
    {:type :die, :num-faces 5, :value 3 :discarded false :previous-values []}
    {:type :die, :num-faces 5, :value 2 :discarded false :previous-values []}))

(def sample-dice {:type :dice
                  :num-rolls 4
                  :num-faces 5
                  :operation
                  {:type :set-operation
                   :op :keep
                   :selector {:type :set-selector, :criteria :lesser-than, :num 2}}})


(deftest testing-data-structs
  (testing "data structure for selector"
    (is (thrown? java.lang.AssertionError (data-structs/build-selector :keep 2)))
    (is (= (data-structs/build-selector :lesser-than 2) sample-selector)))

  (testing "data structure for operator"
    (is (thrown?
         java.lang.AssertionError
         (data-structs/build-operation :wrong-keyword sample-selector)))
    (is (= (data-structs/build-operation :keep sample-selector) sample-operation)))

  (testing "data structure for die(not dice!)"
    ;; the num-faces are less than the value so it should throw an error
    (is (thrown?
         java.lang.AssertionError
         (data-structs/build-die 5 6) sample-die))
    (is (= (data-structs/build-die 5 3) sample-die)))

  (testing "generated values for die"
    (with-redefs [utils/gen-rand-int (fn [x] x)]
      (is (= (data-structs/generate-die-values 4 5) sample-values-for-die))))

  (testing "data structure for dice"
    (is (= (data-structs/build-dice 4 5 sample-operation) sample-dice))))

(deftest operations-on-set
  (testing "keep"
    (testing "only the matched values"
      (is (= (dies-factory [false true false true] [[] [] [] []])
             (dice-roller/keep-in-set sample-values-for-die :equals 3))))
    (testing "values smaller than X"
      (is (= (dies-factory [true true true false] [[] [] [] []])
             (dice-roller/keep-in-set sample-values-for-die :lesser-than 3))))
    (testing "values greater than X"
      (is (= (dies-factory [true false true true] [[] [] [] []])
             (dice-roller/keep-in-set sample-values-for-die :greater-than 3))))
    (testing "X smallest values"
      (is (= (dies-factory [false true false false] [[] [] [] []])
             (dice-roller/keep-in-set sample-values-for-die :lowest 3))))
    (testing "X largest values"
      (is (= (dies-factory [false false true true] [[] [] [] []])
             (dice-roller/keep-in-set sample-values-for-die :highest 2)))))

  (testing "drop"
    (testing "only the matched values"
      (is (= (dies-factory [false true false false] [[] [] [] []])
             (dice-roller/drop-in-set sample-values-for-die :equals 5))))
    (testing "values smaller than X"
      (is (= (dies-factory [false false false true] [[] [] [] []])
             (dice-roller/drop-in-set sample-values-for-die :lesser-than 3))))
    (testing "values greater than X"
      (is (= (dies-factory [false true false false] [[] [] [] []])
             (dice-roller/drop-in-set sample-values-for-die :greater-than 3))))
    (testing "X smallest values"
      (is (= (dies-factory [true false true true] [[] [] [] []])
             (dice-roller/drop-in-set sample-values-for-die :lowest 3))))
    (testing "X largest values"
      (is (= (dies-factory [true true false false] [[] [] [] []])
             (dice-roller/drop-in-set sample-values-for-die :highest 2)))))

  #_(testing "reroll"
      (testing "only the matched values"
        (is (not (some #(= % 3) (dice-roller/reroll-in-set sample-values-for-die :equals 3)))))
      (testing "values smaller than X"
        (is (not (some #(< % 3) (dice-roller/reroll-in-set sample-values-for-die :lesser-than 3)))))
      (testing "values greater than X"
        (is (not (some #(> % 3) (dice-roller/reroll-in-set sample-values-for-die :greater-than 3)))))
      #_(testing "X smallest values"
          (is (= (dice-roller/reroll-in-set sample-values-for-die :lowest 3) (5 5))))
      #_(testing "X largest values"
          (is (= (dice-roller/reroll-in-set '(1 2 3 1 2 3) :highest 3) '(1 1 2))))
      (testing "throws error when the expression re-rolls more than 500 times"
        (let [values4d1 (data-structs/generate-die-values 4 1)]
          (is (thrown? java.lang.AssertionError
                       (dice-roller/reroll-in-set values4d1 :lowest 4)))))))
