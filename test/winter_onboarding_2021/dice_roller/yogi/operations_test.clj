(ns winter-onboarding-2021.dice-roller.yogi.operations-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.dice-roller.yogi.operations :as operations]))
  


(deftest keep-test
  (testing "testing keep"
    (is (= (operations/my-keep 2 '(3 2 2 2 5)) '(2 2 2)))
    (is (= (operations/my-keep '(2 3) '(3 2 2 2 5)) '(3 2 2 2)))))

(deftest drop-test
  (testing "testing drop"
    (is (= '(3 5) (operations/my-drop 2 '(3 2 2 5))))
    (is (= '(3 5) (operations/my-drop '(1 2) '(1 2 3 2 1 5))))))

