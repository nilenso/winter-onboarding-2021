(ns winter-onboarding-2021.fleet-management-service.views.user-test
  (:require [clojure.test :refer [deftest is testing]]
            [hiccup-find.core :as hf]
            [winter-onboarding-2021.fleet-management-service.views.user :as view]))

(deftest signup-form
  (testing "Should return inputs for email,name and password"
    (is (= 4
           (count (hf/hiccup-find [:input] (view/signup-form)))))
    (is (= "Email address"
           (hf/hiccup-text (hf/hiccup-find [:#email] (view/signup-form)))))
    (is (= "Name"
           (hf/hiccup-text (hf/hiccup-find [:#name] (view/signup-form)))))
    (is (= "Password"
           (hf/hiccup-text (hf/hiccup-find [:#password] (view/signup-form)))))))
