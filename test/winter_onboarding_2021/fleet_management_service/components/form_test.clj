(ns winter-onboarding-2021.fleet-management-service.components.form-test
  (:require [clojure.test :refer [deftest is testing]]
            [hiccup-find.core :as hf]
            [winter-onboarding-2021.fleet-management-service.components.form :as form]))

(deftest form
  (testing "renders an empty form if no arguments are passed"
    (is (= [:form {} nil]
           (form/form {}))))
  (testing "renders a form for the given action"
    (is (= [:form {:action "/fleets"} nil]
           (form/form {:action "/fleets"}))))
  (testing "renders a form for the given method"
    (is (= [:form {:method "POST"} nil]
           (form/form {:method "POST"}))))
  (testing "renders a form for the given enctype"
    (is (= [:form {:enctype "multipart/form-data"} nil]
           (form/form {:enctype "multipart/form-data"}))))
  (testing "renders a form for the given action, method, enctype"
    (is (= [:form {:action "/fleets" :method "POST" :enctype "multipart/form-data"} nil]
           (form/form {:action "/fleets"
                       :method "POST"
                       :enctype "multipart/form-data"}))))
  (testing "renders an input element for each given input"
    (is (= [:label {:for "foo-id"
                    :class "form-label"} "foo"]
           (first (hf/hiccup-find [:label]
                                  (form/form {:inputs [{:label "foo"
                                                        :id "foo-id"
                                                        :name "name"
                                                        :value "foobar"}]})))))
    (is (= [:input {:type "text"
                    :name "name"
                    :class "form-control"
                    :value "foobar"}]
           (first (hf/hiccup-find [:input]
                                  (form/form {:inputs [{:label "Name"
                                                        :type "text"
                                                        :name "name"
                                                        :value "foobar"}]})))))
    (is (= nil
           (first (hf/hiccup-find [:label]
                                  (form/form {:inputs [{:type "text"
                                                        :name "name"
                                                        :value "foobar"}]}))))))
  (testing "renders a submit button"
    (is (= [:input {:type "submit"
                    :class "btn btn-primary"
                    :value "Submit"}]
           (first (hf/hiccup-find [:input]
                                  (form/form {:inputs [{:type "submit"
                                                        :class "btn btn-primary"
                                                        :value "Submit"}]})))))))
