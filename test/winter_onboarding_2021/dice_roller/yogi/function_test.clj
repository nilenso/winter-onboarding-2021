(ns winter-onboarding-2021.dice-roller.yogi.function-test
   (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.yogi.functions :as functions]))

(deftest keep-test
  (testing "testing keep"
    (is (= (functions/k 2 '(3 2 2 2 5)) '(2 2 2)))
    (is (= (functions/k '(2 3) '(3 2 2 2 5)) '(3 2 2 2)))))

(deftest drop-test
  (testing "testing drop"
    (is (= '(3 5) (functions/d 2 '(3 2 2 5)) ))
    (is (= '(3 5) (functions/d '(1 2) '(1 2 3 2 1 5)) ))))







;(functions/d 2 '(1 2 3 4))