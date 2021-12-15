(ns winter-onboarding-2021.fleet-management.views.layout-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.string :as string]
            [hiccup.page :refer [html5]]
            [hiccup-find.core :as hf]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]))

(deftest application
  (testing "Should contain bootstrap files & the page title"
    (let [output (html5 (layout/application {}
                                            "Test Title"
                                            [:div]))]
      (is (string/includes? output "Test Title"))
      (is (string/includes? output
                            "<link href=\"/public/css/bootstrap.min.css\" rel=\"stylesheet\" type=\"text/css\">"))
      (is (string/includes? output
                            "<script src=\"/public/js/jquery-3.6.0.min.js\" type=\"text/javascript\"></script>"))
      (is (string/includes? output
                            "<script src=\"/public/js/bootstrap.bundle.min.js\" type=\"text/javascript\"></script>")))))

(deftest contains-navbar
  (testing "Should contain navbar"
    (is (= (count (hf/hiccup-find [:header] (layout/application {} "Test Page" "")))
           1))))
