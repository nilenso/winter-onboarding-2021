(ns winter-onboarding-2021.dice-roller.shivam-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.shivam.data-structs :as data-structs]
            [winter-onboarding-2021.dice-roller.shivam.core :as dice-roller]))

(def sample-selector {:type "SetSelector"
                      :category "l"
                      :num 2})

(def sample-operation {:type "SetOperation"
                       :op "k"
                       :selector sample-selector})

(def sample-die {:type "Die"
                 :num-faces 5
                 :value 3})

(def sample-values-for-die
  '({:type "Die", :num-faces 5, :value 5}
    {:type "Die", :num-faces 5, :value 1}
    {:type "Die", :num-faces 5, :value 2}
    {:type "Die", :num-faces 5, :value 1}))

(def sample-dice {:type "Dice"
                  :num-rolls 4
                  :num-faces 5
                  :values
                  '({:type "Die", :num-faces 5, :value 5}
                    {:type "Die", :num-faces 5, :value 1}
                    {:type "Die", :num-faces 5, :value 2}
                    {:type "Die", :num-faces 5, :value 1})
                  :operation
                  {:type "SetOperation"
                   :op "k"
                   :selector {:type "SetSelector", :category "l", :num 2}}})


(deftest testing-data-structs
  (testing "data structure for selector"
    (is (thrown? java.lang.AssertionError (data-structs/build-selector "k" 2)))
    (is (= (data-structs/build-selector "l" 2) sample-selector)))

  (testing "data structure for operator"
    (is (thrown?
         java.lang.AssertionError
         (data-structs/build-operation "s" sample-selector)))
    (is (= (data-structs/build-operation "k" sample-selector) sample-operation)))

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
  (testing "keep elements with a certain criteria"
    ;; keeping only the matched value 
    (is (= (dice-roller/keep-in-set '(1 2 3 1 2 3) "" 3) '(3 3)))
    ;; keeping values smaller than X
    (is (= (dice-roller/keep-in-set '(1 2 3 1 2 3) "<" 3) '(1 2 1 2)))
    ;; keeping values greater than X
    (is (= (dice-roller/keep-in-set '(1 2 3 1 2 3) ">" 3) '()))
    ;; keeping X smallest values
    (is (= (dice-roller/keep-in-set '(1 2 3 1 2 3) "l" 3) '(1 2 1 2)))
    ;; keeping X largest values
    (is (= (dice-roller/keep-in-set '(1 2 3 1 2 3) "k" 3) '(1 2 1 2)))))

;; TODO
;; 1. Instead of definign the whole func, try to only redef the func which causes the side effects
;; 2. Instead of writing "s" "k" strings, try to use keywords