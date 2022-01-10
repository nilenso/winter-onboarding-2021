(ns winter-onboarding-2021.fleet-management-service.views.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [hiccup-find.core :as hf]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.views.fleet :as views]
            [winter-onboarding-2021.fleet-management-service.db.fleet-test :as fleet-db-test]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(defn attributes [element]
  (when (map? (second element))
    (second element)))

(deftest create
  (testing "Should return us a form asking for the fleet name"
    (let [form-view (views/create-fleet)
          form (first (hf/hiccup-find [:form] form-view))
          input (first (hf/hiccup-find [:input] form-view))]
      (is (= "/fleets" (:action (attributes form))))
      (is (= "POST" (:method (attributes form))))
      
      (is (= "name" (:name (attributes input))))
      (is (= "" (:value (attributes input)))))))

(deftest show-fleets
  (testing "Should return us a list of hiccup divs with fleet data"
    (let [{:keys [fleets]} (fleet-db-test/seed-user-fleets-db 5)
          fleets-with-managers (map #(assoc %
                                            :fleets/created-at (java.util.Date.)
                                            :managers
                                            [{:users/name "Manager 1"}])
                                    fleets)
          lists-view (views/show-fleets fleets-with-managers 1 false)
          headers (hf/hiccup-find [:thead :tr :th] lists-view)
          fleets-list (hf/hiccup-find [:tbody :tr] lists-view)]
      
      (is (= "Name" (second (first headers))))
      (is (= "Managers" (second (second headers))))
      (is (= "Created At" (second (nth headers 2))))
      
      (is (= 5 (count fleets-list))))))
