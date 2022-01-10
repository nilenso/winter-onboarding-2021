(ns winter-onboarding-2021.fleet-management.views.fleet-test
  (:require [clojure.test :refer [deftest is testing]]
            [hiccup-find.core :as hf]
            [winter-onboarding-2021.fleet-management-service.views.fleet :as views]))

(defn attributes [element]
  (second element))

(deftest create
  (testing "Should return us a form asking for the fleet name"
    (let [form-view (views/create-fleet)
          form (first (hf/hiccup-find [:form] form-view))
          input (first (hf/hiccup-find [:input] form-view))]
      (is (= "/fleets" (:action (attributes form))))
      (is (= "POST" (:method (attributes form))))
      
      (is (= "name" (:name (attributes input))))
      (is (= "" (:value (attributes input)))))))
