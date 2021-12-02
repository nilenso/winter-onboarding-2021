(ns winter-onboarding-2021.fleet-management.handlers.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [clojure.string :as str]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as handlers]
            [winter-onboarding-2021.fleet-management-service.views.cab :as views]
            [winter-onboarding-2021.fleet-management-service.models.cab :as models]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest add-cab
  (testing "POST /cabs/ endpoint with valid cab data, shoudld redirect to '/cabs/<<uuid of new cab>> "
    (let [response (handlers/create {:multipart-params
                                     {:name "Test cab"
                                      :licence_plate "KA20X1234"
                                      :distance_travelled "1223"}})]
      (is (= 302 (:status response)))

      (is (= {:success true
              :style-class "alert alert-success"
              :message "Cab added successfully!"}
             (:flash response)))

      (is (= true (-> response
                      (get-in [:headers "Location"])
                      (str/split #"/") ; Could be better if we use regex
                      (nth 2)
                      count
                      (= 36))))

      (is (= "" (:body response))))))

(testing "POST /cabs/ endpoint with invalid cab data, should redirect to '/cabs/new"
  (let [invalid-cab {:name "Test cab"
                     :licence_plate "KA20X1234"}]
    (is (= {:status 302
            :flash {:error true
                    :style-class "alert alert-danger"
                    :message "Could not add cab, try again!"
                    :data invalid-cab}
            :headers {"Location" "/cabs/new"}
            :body ""}
           (handlers/create {:multipart-params invalid-cab})))))

(deftest view-single-cab
  (testing "Should return 200 code with the HTML of the single cab details view"
    (let [cab (models/create {:name "Foo cab"
                              :licence-plate "Foo Licence Plate"
                              :distance-travelled 19191})
          output-html (layout/application {}
                                          (:cabs/name cab)
                                          (views/single-cab cab))]
      (is (= {:status 200
              :body output-html}
             (-> {:params {:slug (str (:cabs/id cab))}}
                 handlers/view-cab
                 (select-keys [:status :body])))))))
