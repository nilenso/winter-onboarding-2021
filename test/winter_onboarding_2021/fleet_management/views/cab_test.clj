(ns winter-onboarding-2021.fleet-management.views.cab-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.string :as str]
            [winter-onboarding-2021.fleet-management-service.views.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]))

(deftest add-cab-view
  (testing "Should have alert success bubble after adding a valid cab"
    (let [success-msg "Cab added successfully!"
          output-html (layout/application
                       {:flash {:success true
                                :style-class "alert alert-success"
                                :message success-msg}}
                       "Add a cab"
                       (cab/cab-form {:licence_plate "" :name "" :distance_travelled ""}))]
      (is (str/includes?
           output-html
           (format
            "<div class=\"alert alert-success\">%s</div>"
            success-msg)))))

  (testing "Should have alert error bubble after trying to add an invalid cab"
    (let [error-msg "Could not add cab, try again!"
          output-html (layout/application
                       {:flash {:error true
                                :style-class "alert alert-danger"
                                :message error-msg}}
                       "Add a cab"
                       (cab/cab-form {:licence_plate "" :name "" :distance_travelled ""}))]
      (is (str/includes?
           output-html
           (format "<div class=\"alert alert-danger\">%s</div>" error-msg))))))
