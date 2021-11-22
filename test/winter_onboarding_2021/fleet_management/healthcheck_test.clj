(ns winter-onboarding-2021.fleet-management.healthcheck-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [mount.core :as mount]
            [winter-onboarding-2021.fleet-management-service.handler :as handlers]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)


(deftest healthcheck
  (testing "Healthcheck for DB"
    (is (= {:status 200
            :headers {}
            :body {:message "Everything OK!"
                   :data [{:check 1}]}}
           (handlers/health-check {})))))
