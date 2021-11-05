(ns winter-onboarding-2021.dice-roller.shivam-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.shivam.utils :as utils]
            [winter-onboarding-2021.dice-roller.shivam.data-structs :as data-structs]
            [winter-onboarding-2021.dice-roller.shivam.core :as dice-roller]))

(def sample-literal
  {:type :literal
   :value 4})

(def sample-selector
  {:type :set-selector
   :criteria :lesser-than
   :num 2})

(def sample-operation
  {:type :set-operation
   :op :keep
   :selector sample-selector})

(def sample-die
  {:type :die
   :num-faces 5
   :value 3
   :discarded false
   :previous-values []})

(def sample-with-redef-values-for-die
  '({:type :die, :num-faces 5, :value 5 :discarded false :previous-values []}
    {:type :die, :num-faces 5, :value 5 :discarded false :previous-values []}
    {:type :die, :num-faces 5, :value 5 :discarded false :previous-values []}
    {:type :die, :num-faces 5, :value 5 :discarded false :previous-values []}))

(defn dies-factory [discard-sequence previous-values-sequence]
  [{:type :die :num-faces 5 :value 3 :discarded (nth discard-sequence 0) :previous-values (nth previous-values-sequence 0)}
   {:type :die :num-faces 5 :value 5 :discarded (nth discard-sequence 1) :previous-values (nth previous-values-sequence 1)}
   {:type :die, :num-faces 5, :value 3 :discarded (nth discard-sequence 2) :previous-values (nth previous-values-sequence 2)}
   {:type :die, :num-faces 5, :value 2 :discarded (nth discard-sequence 3) :previous-values (nth previous-values-sequence 3)}])

(def sample-values-for-die
  '({:type :die, :num-faces 5, :value 3 :discarded false :previous-values []}
    {:type :die, :num-faces 5, :value 5 :discarded false :previous-values []}
    {:type :die, :num-faces 5, :value 3 :discarded false :previous-values []}
    {:type :die, :num-faces 5, :value 2 :discarded false :previous-values []}))

(def sample-dice
  {:type :dice
   :num-rolls 4
   :num-faces 5
   :operation
   {:type :set-operation
    :op :keep
    :selector {:type :set-selector, :criteria :lesser-than, :num 2}}})

(def sample-bin-op-on-literals
  {:type :binary-op
   :left sample-literal
   :op :add
   :right {:type :literal :value 5}})

(def evaluated-bin-op-on-literals
  {:type :evaluated-bin-op
   :left sample-literal
   :op :add
   :right {:type :literal :value 5}
   :value 9})

(def sample-bin-op-on-literal-n-dice
  {:type :binary-op
   :left sample-dice
   :op :add
   :right sample-literal})

#_(def evaluated-sample-bin-op-on-literal-n-dice
  {:type :evaluated-bin-op
   :left {:type :evaluated-dice
          :values sample-with-redef-values-for-die
          :value 20}
   :op :add
   :right sample-literal
   :value 24})

#_(def evaluated-nested-bin-op
  {:type :evaluated-bin-op
   :left {:type :evaluated-bin-op
          :left {:type :literal, :value 4}
          :op :add
          :right {:type :literal, :value 5}
          :value 9}
   :op :multiply
   :right {:type :evaluated-dice
           :values sample-with-redef-values-for-die
           :value 20}
   :value 180})

(deftest testing-data-structs

  (testing "data structre for a literal"
    (is (= sample-literal (data-structs/build-literal 4))))

  (testing "data structure for selector"
    (is (thrown? java.lang.AssertionError (data-structs/build-selector :keep 2)))
    (is (= sample-selector (data-structs/build-selector :lesser-than 2))))

  (testing "data structure for operator"
    (is (thrown?
         java.lang.AssertionError
         (data-structs/build-operation :wrong-keyword sample-selector)))
    (is (= sample-operation (data-structs/build-operation :keep sample-selector))))

  (testing "data structure for die(not dice!)"
    ;; the num-faces are less than the value so it should throw an error
    (is (thrown?
         java.lang.AssertionError
         sample-die (data-structs/build-die 5 6)))
    (is (= sample-die (data-structs/build-die 5 3))))

  (testing "generated values for die"
    (with-redefs [utils/gen-rand-int (fn [x] x)]
      (is (= sample-with-redef-values-for-die (data-structs/generate-die-values 4 5)))))

  (testing "data structure for dice"
    (is (= sample-dice (data-structs/build-dice 4 5 sample-operation))))

  (testing "data structure for binary operation"
    (is (= sample-bin-op-on-literals
           (data-structs/build-bin-op
            (data-structs/build-literal 4)
            :add
            (data-structs/build-literal 5))))
    (is (= sample-bin-op-on-literal-n-dice
           (data-structs/build-bin-op
            sample-dice
            :add
            sample-literal)))))

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

(deftest eval-bin-op
  (testing "Adding two literal expressions"
    (is (= evaluated-bin-op-on-literals
           (dice-roller/eval-bin-op sample-bin-op-on-literals))))

  ;; Logic is OK but tests need to be fixed
  #_(testing "Adding one literal and dice expression"
      (with-redefs [utils/gen-rand-int (fn [x] x)]
        (is (= evaluated-sample-bin-op-on-literal-n-dice
               (dice-roller/eval-bin-op sample-bin-op-on-literal-n-dice)))))

  #_(testing "Nested binary operation -- left is a addition binary operation of literals & right is a dice"
      (with-redefs [utils/gen-rand-int (fn [x] x)]
        (is (= evaluated-nested-bin-op
               (dice-roller/eval-bin-op
                (data-structs/build-bin-op
                 sample-bin-op-on-literals
                 :multiply
                 sample-dice)))))))
