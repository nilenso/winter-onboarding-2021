(ns winter-onboarding-2021.fleet-management.models.user-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-model]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-cab
  (testing "Should create a cab"
    (let [user {:name "Harry Potter"
                :role "admin"
                :email "harry@hogwarts.edu"
                :password "hermione@123"}
          _ (user-model/create user)]
      (is (= #:users{:name "Harry Potter"
                     :role "admin"
                     :email "harry@hogwarts.edu"
                     :password "hermione@123"}
             (dissoc (first (db-core/query! ["SELECT * FROM users;"]))
                     :users/id))))))

