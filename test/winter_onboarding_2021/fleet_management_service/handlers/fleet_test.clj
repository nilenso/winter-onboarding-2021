(ns winter-onboarding-2021.fleet-management.handlers.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.handlers.fleet :as fleet-handler]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management-service.db.user :as user-db]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-fleet
  (testing "Should create a fleet by an admin"
    (let [user {:users/name "foo bar"
                :users/role "admin"
                :users/email "foobar@gmail.com"
                :users/password "foo"}
          user-id (:users/id (user-db/create user))
          request {:form-params {:name "Goo fleet"}
                   :user #:users{:id user-id
                                 :name "foo bar"
                                 :role "admin"
                                 :email "foobar@gmail.com"}}
          response (fleet-handler/create-fleet request)
          inserted-fleet (first (db-core/query! ["SELECT * FROM FLEETS"]))]
      (is (= 302 (:status response)))
      (is (= #:fleets{:name "Goo fleet"
                      :created-by user-id} (select-keys inserted-fleet 
                                                        [:fleets/name :fleets/created-by]))))))
