(ns winter-onboarding-2021.fleet-management.views.cab-test
  (:require [clojure.test :refer [deftest is testing]]
            [hiccup-find.core :as hf]
            [clojure.string :as str]
            [hiccup-find.core :as hf]
            [hiccup.page :refer [html5]]
            [winter-onboarding-2021.fleet-management-service.views.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]))

(deftest add-cab-view
  (testing "Should have alert success bubble after adding a valid cab"
    (let [success-msg "Cab added successfully!"
          hiccup-output (layout/application
                         {:flash {:success true
                                  :style-class "alert alert-success"
                                  :message success-msg}}
                         "Add a cab"
                         (cab/cab-form {:licence-plate "" :name "" :distance-travelled ""}))]
      (is (= success-msg
             (hf/hiccup-text (hf/hiccup-find [:.alert.alert-success] hiccup-output))))))

  (testing "Should have alert error bubble after trying to add an invalid cab"
    (let [error-msg "Could not add cab, try again!"
          hiccup-output (layout/application
                         {:flash {:error true
                                  :style-class "alert alert-danger"
                                  :message error-msg}}
                         "Add a cab"
                         (cab/cab-form {:licence-plate "" :name "" :distance-travelled ""}))]
      (is (= error-msg
             (hf/hiccup-text (hf/hiccup-find [:.alert.alert-danger] hiccup-output)))))))

(deftest view-single-cab
  (testing "Should have the details like name, licence plate and distance travelled of single cab"
    (let [cab #:cabs{:name "Maruti Cab"
                     :licence-plate "HR20A 1234"
                     :distance-travelled 12333}
          hiccup-output (layout/application
                         {} ; request is empty
                         (format "Cab %s" (:cabs/name cab))
                         (cab/cab cab))]

      (is (= (hf/hiccup-text (hf/hiccup-find [:h2] hiccup-output))
             (:cabs/name cab)))

      (is (= (hf/hiccup-text (first (hf/hiccup-find [:h3] hiccup-output)))
             (:cabs/licence-plate cab)))

      (is (= (hf/hiccup-text (second (hf/hiccup-find [:h3] hiccup-output)))
             (str (:cabs/distance-travelled cab)))))))

(deftest update-cab-form
  (testing "Should have form labels name, licence plate and distance travelled"
    (let [cab #:cabs{:id (java.util.UUID/randomUUID)
                     :name "Maruti Cab"
                     :licence-plate "HR20A 1234"
                     :distance-travelled 12333}
          output-view (cab/update-cab-form cab)]
      (is (= 3 (count (hf/hiccup-find [:input.form-control] output-view))))
      (is (str/includes? (html5 output-view) (:cabs/name cab)))
      (is (str/includes? (html5 output-view) (str (:cabs/distance-travelled cab))))
      (is (str/includes? (html5 output-view) (:cabs/licence-plate cab))))))

(deftest delete-modal
  (testing "Should have name of the cab in its title"
    (let [output (cab/modal {:modal-id "deleteCabModal"
                             :title "Delete Foo Cab?"
                             :body "Once the cab is deleted, it can't be recovered."
                             :footer (list [:button
                                            {:data-bs-dismiss "modal", :type "button" :class "btn btn-secondary"} "Close"]
                                           [:form {:action "/cabs/delete" :method "POST"}
                                            [:input {:name "id" :value (java.util.UUID/randomUUID) :hidden true}]
                                            [:button {:type "submit" :class "btn btn-danger"} "Delete"]])})
          hiccup-find-result (hf/hiccup-find [:h5.modal-title] output)
          result-string (hf/hiccup-text hiccup-find-result)]
      (is (= "Delete Foo Cab?" result-string)))))
