(ns winter-onboarding-2021.fleet-management-service.healthcheck-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.handlers.core :as handlers]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest healthcheck
  (testing "Healthcheck for DB"
    (is (= {:status 200
            :headers {}
            :body {:message "Everything OK!"
                   :data [{:check 1}]}}
           (handlers/health-check {})))))
