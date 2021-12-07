(ns winter-onboarding-2021.fleet-management.views.cab-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.string :as str]
            [winter-onboarding-2021.fleet-management-service.views.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]))

(deftest header-formatting
  (testing "Should convert a namespaced keyword into human string"
    (is (= "Distance travelled"
           (cab/format-header :cabs/distance-travelled)))))

(deftest add-cab-view
  (testing "Should have alert success bubble after adding a valid cab"
    (let [success-msg "Cab added successfully!"
          output-html (layout/application
                       {:flash {:success true
                                :style-class "alert alert-success"
                                :message success-msg}}
                       "Add a cab"
                       (cab/cab-form {:licence-plate "" :name "" :distance-travelled ""}))]
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
                       (cab/cab-form {:licence-plate "" :name "" :distance-travelled ""}))]
      (is (str/includes?
           output-html
           (format "<div class=\"alert alert-danger\">%s</div>" error-msg))))))

(deftest view-single-cab
  (testing "Shold have the details like name, licence plate and distance travelled of single cab"
    (let [cab #:cabs{:name "Maruti Cab"
                     :licence-plate "HR20A 1234"
                     :distance-travelled 12333}
          output-html (layout/application
                       {} ; request is empty
                       (format "Cab %s" (:name cab))
                       (cab/cab cab))]

      (is (str/includes? output-html (:cabs/name cab)))

      (is (str/includes? output-html (:cabs/licence-plate cab)))

      (is (str/includes? output-html (str (:cabs/distance-travelled cab)))))))
