(ns winter-onboarding-2021.dice-roller.yogi.selectors-test
  (:require [clojure.test :refer [is testing deftest]]
            [winter-onboarding-2021.dice-roller.yogi.selectors :as selectors]))

(deftest take-highest-n
  (testing "testing take-highest-n"
    (is (= '(54 47) (selectors/highest 2 '(1 2 3 54 1 47 12))))
    (is (= '(54 47 12) (selectors/highest 3 '(1 2 3 54 1 47 12))))
    (is (= '(54 47) (selectors/highest 2 '(1 2 3 54 1 47 12))))))


(deftest take-lowest-n
  (testing "testing take-lowest-n"
    (is (= '(1 1) (selectors/lowest 2 '(1 2 3 54 1 47 12))))
    (is (= '(1 1 2) (selectors/lowest 3 '(1 2 3 54 1 47 12))))))

(deftest lower-than-n
  (testing "testing lower than"
    (is (= '(2 7 7) (selectors/lower 9 '(2 15 7 52 10 356 7))))
    (is (= '() (selectors/lower 1 '(1 2 3 5 4 1  2 3))))))


(deftest greater-than-n
  (testing "testing greater than"
    (is (= '(15 52 10 356) (selectors/greater 9 '(2 15 7 52 10 356 7))))
    (is (= '() (selectors/greater 100 '(1 2 3 5 4 1  2 3))))))
