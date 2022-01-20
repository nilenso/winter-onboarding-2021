(ns winter-onboarding-2021.fleet-management-service.views.organisation-test
  (:require [clojure.test :refer [deftest is testing]]
            [hiccup-find.core :as hf]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.views.organisation :as org-views]))

(deftest create
  (testing "Should show the view to an admin for creating a fleet"
    (let [create-view (org-views/create)
          form (first (hf/hiccup-find [:form] create-view))
          name-input (first (hf/hiccup-find [:form :input] create-view))
          name-input-attrs (utils/hiccup-attributes name-input)]
      (is (= "/organisations/new" (:action (utils/hiccup-attributes form))))
      (is (= "POST" (:method (utils/hiccup-attributes form))))

      (is (= "name" (:name name-input-attrs)))
      (is (true? (:required name-input-attrs))))))
