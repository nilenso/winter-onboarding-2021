(ns winter-onboarding-2021.fleet-management.handlers.user-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.handlers.user :as handler]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest user-create
  (testing "Should create a user in db"
    (let [user {:name "Severus Snape"
                :email "s.snape@hogwarts.edu"
                :password "lily"}
          _ (handler/create-user {:multipart-params user})]
      (is (= #:users{:name "Severus Snape"
              :role "admin"
              :email "s.snape@hogwarts.edu"
              :password "lily"}
             (dissoc (first (db-core/query! ["SELECT * FROM users;"])) :users/id))))))
