(ns winter-onboarding-2021.fleet-management-service.handlers.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.handlers.fleet :as fleet-handler]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-fleet
  (testing "Should create a fleet by an admin"
    (let [user (factories/admin)
          user-id (:users/id user)
          request {:form-params {:name "Goo fleet"}
                   :user user}
          response (fleet-handler/create-fleet request)
          inserted-fleet (first (db-core/query! ["SELECT * FROM FLEETS"]))]
      (is (= 302 (:status response)))
      (is (= #:fleets{:name "Goo fleet"
                      :created-by user-id} (select-keys inserted-fleet
                                                        [:fleets/name :fleets/created-by])))
      (is (= {:success true
              :style-class "alert alert-success"
              :message "Fleet created successfully!"} (:flash response)))))

  (testing "Should not create a fleet if the admin is not logged-in"
    (let [request {:form-params {:name "Boo fleet"}}
          response (fleet-handler/create-fleet request)]
      (is (= 302 (:status response)))
      (is (= {"Location" "/fleets/new"} (:headers response)))
      (is (= {:error true
              :style-class "alert alert-danger"
              :message "Could not create fleet, try again!"} (:flash response))))))
