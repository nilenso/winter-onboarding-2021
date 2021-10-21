(ns winter-onboarding-2021.dice-roller.yogi.function-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.yogi.functions :as functions]))

(deftest keep-test
  (testing "testing keep"
    (is (= (functions/k 2 '(3 2 2 2 5)) '(2 2 2)))
    (is (= (functions/k '(2 3) '(3 2 2 2 5)) '(3 2 2 2)))))

(deftest drop-test
  (testing "testing drop"
    (is (= '(3 5) (functions/d 2 '(3 2 2 5))))
    (is (= '(3 5) (functions/d '(1 2) '(1 2 3 2 1 5))))))

(deftest take-highest-n
  (testing "testing take-highest-n"
    (is (= '(54 47) (functions/h 2 '(1 2 3 54 1 47 12))))
    (is (= '(54 47 12) (functions/h 3 '(1 2 3 54 1 47 12))))
    (is (= '(54 47) (functions/h 2 '(1 2 3 54 1 47 12))))))


(deftest take-lowst-n
  (testing "testing take-lowest-n"
    (is (= '(1 1) (functions/l 2 '(1 2 3 54 1 47 12))))
    (is (= '(1 1 2) (functions/l 3 '(1 2 3 54 1 47 12))))))

(def totest '(functions/k '(functions/l 2 '(functions/my-roller 3 6)) '(functions/my-roller 3 6)))

(eval totest)








;(functions/d 2 '(1 2 3 4))