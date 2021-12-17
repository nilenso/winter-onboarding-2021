(ns winter-onboarding-2021.fleet-management.routes-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [crypto.password.bcrypt :as password]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as cab-handlers]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.routes :as routes]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest authorization
  (testing "Should allow access to cabs list for a logged-in admin user & not redirect to dashboard"
    (let [user #:users{:name "Severus Snape"
                       :email "foo@gmail.com"
                       :password (password/encrypt "lily")
                       :role "admin"}
          cabs-response ((routes/protect cab-handlers/get-cabs #{:admin :manager})
                         {:user user})]
      (is (not= 302 (:status cabs-response)))))

  (testing "Should not allow access to cabs list for a logged-in user who is a driver & should redirect to dashboard"
    (let [user #:users{:name "Severus Snape"
                       :email "foo@gmail.com"
                       :password (password/encrypt "lily")
                       :role "driver"}
          cabs-response ((routes/protect cab-handlers/get-cabs #{:admin :manager})
                         {:user user})]
      (is (= 302 (:status cabs-response)))
      (is (= "/users/dashboard" (get-in cabs-response [:headers "Location"]))))))
