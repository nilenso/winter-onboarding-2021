(ns winter-onboarding-2021.dice-roller.shivam-test
  (:require [clojure.test :refer [deftest is testing]]
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

(def sample-values-for-die
  '({:type :die, :num-faces 5, :value 5}
    {:type :die, :num-faces 5, :value 1}
    {:type :die, :num-faces 5, :value 2}
    {:type :die, :num-faces 5, :value 1}))

(def sample-dice {:type :dice
                  :num-rolls 4
                  :num-faces 5
                  :values
                  '({:type :die, :num-faces 5, :value 5}
                    {:type :die, :num-faces 5, :value 1}
                    {:type :die, :num-faces 5, :value 2}
                    {:type :die, :num-faces 5, :value 1})
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
    (with-redefs [data-structs/generate-die-values (fn [num-rolls num-faces] sample-values-for-die)]
      (is (= (data-structs/generate-die-values 4 5) sample-values-for-die))))

  (testing "data structure for dice"
    (is (= (data-structs/build-dice 4 5 sample-values-for-die sample-operation)
           sample-dice))))

(deftest operations-on-set
  (testing "keep"
    (testing "only the matched values"
      (is (= (dice-roller/keep-in-set '(1 2 3 1 2 3) :equals 3) '(3 3))))
    (testing "values smaller than X"
      (is (= (dice-roller/keep-in-set '(1 2 3 1 2 3) :lesser-than 3) '(1 2 1 2))))
    (testing "values greater than X"
      (is (= (dice-roller/keep-in-set '(1 2 3 1 2 3) :greater-than 3) '())))
    (testing "X smallest values"
      (is (= (dice-roller/keep-in-set '(1 2 3 1 2 3) :lowest 3) '(1 1 2))))
    (testing "X largest values"
      (is (= (dice-roller/keep-in-set '(1 2 3 1 2 3) :highest 3) '(2 3 3)))))

  (testing "drop"
    (testing "only the matched values"
      (is (= (dice-roller/drop-in-set '(1 2 3 1 2 3) :equals 3) '(1 2 1 2))))
    (testing "values smaller than X"
      (is (= (dice-roller/drop-in-set '(1 2 3 1 2 3) :lesser-than 3) '(3 3))))
    (testing "values greater than X"
      (is (= (dice-roller/drop-in-set '(1 2 3 1 2 3) :greater-than 3) '(1 2 3 1 2 3))))
    (testing "X smallest values"
      (is (= (dice-roller/drop-in-set '(1 2 3 1 2 3) :lowest 3) '(3 2 3))))
    (testing "X largest values"
      (is (= (dice-roller/drop-in-set '(1 2 3 1 2 3) :highest 3) '(1 1 2)))))

  #_(testing "reroll"
    (testing "only the matched values"
      (is (= (dice-roller/drop-in-set '(1 2 3 1 2 3) :equals 3) '(1 2 1 2))))
    (testing "values smaller than X"
      (is (= (dice-roller/drop-in-set '(1 2 3 1 2 3) :lesser-than 3) '(3 3))))
    (testing "values greater than X"
      (is (= (dice-roller/drop-in-set '(1 2 3 1 2 3) :greater-than 3) '(1 2 3 1 2 3))))
    (testing "X smallest values"
      (is (= (dice-roller/drop-in-set '(1 2 3 1 2 3) :lowest 3) '(2 3 3))))
    (testing "X largest values"
      (is (= (dice-roller/drop-in-set '(1 2 3 1 2 3) :highest 3) '(1 1 2))))))

;; TODO
;; 1. Instead of definign the whole func, try to only redef the func which causes the side effects
;; 2. Instead of writing "s" "k" strings, try to use keywords