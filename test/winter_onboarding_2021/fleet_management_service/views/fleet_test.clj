(ns winter-onboarding-2021.fleet-management-service.views.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [hiccup-find.core :as hf]
            [clojure.string :as string]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.views.fleet :as views]
            [winter-onboarding-2021.fleet-management-service.db.fleet-test :as fleet-db-test]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(defn attributes [element]
  (when (map? (second element))
    (second element)))

(deftest create
  (testing "Should return us a form asking for the fleet name & no team members"
    (let [form-view (views/create-fleet [])
          form (first (hf/hiccup-find [:form] form-view))
          input (first (hf/hiccup-find [:input] form-view))
          form-string (hf/hiccup-text form)]
      (is (= "/fleets" (:action (attributes form))))
      (is (= "POST" (:method (attributes form))))

      (is (= "name" (:name (attributes input))))
      (is (= "" (:value (attributes input))))

      (is (string/includes? form-string "You have no managers in your organisation"))
      (is (string/includes? form-string "You have no drivers in your organisation"))))

  (testing "Should return us a form asking for the fleet name, managers, drivers"
    (let [managers (gen/sample (gen/fmap #(assoc % :users/role "manager")
                                         (s/gen ::specs/users))
                               3)
          drivers (gen/sample (gen/fmap #(assoc % :users/role "driver")
                                        (s/gen ::specs/users))
                              3)
          form-view (views/create-fleet `(~@managers ~@drivers))
          form (first (hf/hiccup-find [:form] form-view))
          input (first (hf/hiccup-find [:input] form-view))]

      (is (= "/fleets" (:action (attributes form))))
      (is (= "POST" (:method (attributes form))))

      (is (= "name" (:name (attributes input))))
      (is (= "" (:value (attributes input))))

      (is (= 3 (count (hf/hiccup-find [:fieldset#managers :input] form))))
      (is (= 3 (count (hf/hiccup-find [:fieldset#drivers :input] form)))))))

(deftest show-fleets
  (testing "Should return us a list of hiccup divs with fleet data"
    (let [{:keys [fleets]} (fleet-db-test/seed-user-fleets-db 5)
          fleets-with-managers (map #(assoc %
                                            :fleets/created-at (java.util.Date.)
                                            :managers
                                            [{:users/name "Manager 1"}])
                                    fleets)
          lists-view (views/show-fleets fleets-with-managers 1 false false)
          headers (hf/hiccup-find [:thead :tr :th] lists-view)
          fleets-list (hf/hiccup-find [:tbody :tr] lists-view)]

      (is (= "Name" (second (first headers))))
      (is (= "Managers" (second (second headers))))
      (is (= "Created At" (second (nth headers 2))))
      
      (is (= 5 (count fleets-list)))
      
      (is (= 0 (count (hf/hiccup-find [:#fleet-prev-page] lists-view))))
      (is (= 0 (count (hf/hiccup-find [:#fleet-next-page] lists-view)))))))
