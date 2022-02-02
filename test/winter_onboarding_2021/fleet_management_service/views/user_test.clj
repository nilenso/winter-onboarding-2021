(ns winter-onboarding-2021.fleet-management-service.views.user-test
  (:require [clojure.test :refer [deftest is testing]]
            [hiccup-find.core :as hf]
            [winter-onboarding-2021.fleet-management-service.views.user :as view]))

(deftest signup-form
  (testing "Should return inputs for email,name and password"
    (let [signup-form-output (view/signup-form)]
      (is (= 4
             (count (hf/hiccup-find [:input] signup-form-output))))
      (is (= "Email address"
             (hf/hiccup-text (hf/hiccup-find [:#email] signup-form-output))))
      (is (= "Name"
             (hf/hiccup-text (hf/hiccup-find [:#name] signup-form-output))))
      (is (= "Password"
             (hf/hiccup-text (hf/hiccup-find [:#password] signup-form-output))))))
  (testing "Should return hidden input field of token"
    (let [signup-form-output (view/signup-form "THISISTOKEN")]
      (is (= [:input
              {:type "hidden", :name "token", :value "THISISTOKEN", :id "token"}]
             (first (hf/hiccup-find [:#token] signup-form-output)))))))
 
(deftest login-form
  (testing "Should return inputs for email and password"
    (let [login-form-output (view/login-form)]
      (is (= 2
             (count (hf/hiccup-find [:input] login-form-output))))

      (is (= nil (:action (second (first (hf/hiccup-find [:form] login-form-output))))))

      (is (= "Login"
             (hf/hiccup-text (hf/hiccup-find [:h1] login-form-output))))
      (is (= "Email address"
             (hf/hiccup-text (hf/hiccup-find [:#email] login-form-output))))
      (is (= "Password"
             (hf/hiccup-text (hf/hiccup-find [:#password] login-form-output)))))))
