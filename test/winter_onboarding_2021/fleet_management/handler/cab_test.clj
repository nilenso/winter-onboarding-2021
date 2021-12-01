(ns winter-onboarding-2021.fleet-management.handler.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [hiccup-find.core :as hf]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as handler]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.db.cab :as cab]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest list-cabs-handler
  (testing "Should return a list of 3 rows of cabs"
    (let [cabs (factories/create-cabs 3)]
      (doall (map cab/create cabs))
      (is (= 3 (count (hf/hiccup-find [:tbody :tr] (handler/get-cabs {})))))
      (is (= 0 (count (hf/hiccup-find [:a] (handler/get-cabs {})))))))
  
  (testing "Should return a list of 12 rows of cabs with next Page link"
    (let [cabs (factories/create-cabs 12)]
      (doall (map cab/create cabs))
      (is (= 10 (count (hf/hiccup-find [:tbody :tr] (handler/get-cabs {})))))
      (is (= 1 (count (hf/hiccup-find [:a] (handler/get-cabs {})))))))
  
  (testing "Should return 8 rows of cabs in page number 2"
    (is (= 5 (count (hf/hiccup-find [:tbody :tr] (handler/get-cabs {:query-params
                                                                    {:page "2"}})))))
    (is (= 0 (count (hf/hiccup-find [:a] (handler/get-cabs {:query-params
                                                            {:page "2"}}))))))
  
  (testing "Should return 5 colums for :name :distance-travelled :licence-plate 
            :created-at :updated-at"
    (is (= 5 (count (hf/hiccup-find [:thead :tr :th] (handler/get-cabs {})))))))
