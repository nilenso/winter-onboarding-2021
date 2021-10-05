(ns winter-onboarding-2021.4clojure.alisha-test
  (:require [clojure.test :refer [deftest is testing]]
            [winter-onboarding-2021.4clojure.alisha :as solutions]))

(deftest function-list-equal-to-quote-list
  (testing "Checks eqality of lists constructed from from function with quoted form"
    (is (= (solutions/problem4) '(:a :b :c)))))

()