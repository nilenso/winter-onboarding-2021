(ns winter-onboarding-2021.fleet-management-service.handlers.fleet-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [hiccup-find.core :as hf]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.handlers.fleet :as fleet-handler]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management-service.db.fleet-test :as fleet-test-db]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-fleet
  (testing "Should create a fleet by an admin"
    (let [{admin-id :users/id :as admin} (factories/admin)
          {org-id :organisations/id} (factories/organisation {:organisations/created-by admin-id})
          request {:form-params {:name "Foo fleet"}
                   :user (assoc admin :users/org-id org-id)}
          response (fleet-handler/create-fleet request)
          inserted-fleet (first (db-core/query! ["SELECT * FROM FLEETS"]))]
      (is (= 302 (:status response)))
      (is (= #:fleets{:name "Foo fleet"
                      :created-by admin-id} (select-keys inserted-fleet
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

(deftest show-fleets
  (let [{:keys [user]} (fleet-test-db/seed-user-fleets-db 15)]
    (testing "Should return us a list of 10 fleets with a next page link & no previous page link"
      (let [request {:user user}
            {:keys [title content]} (fleet-handler/show-fleets request)]

        (is (= "List of fleets" title))

        (is (= 10 (count (hf/hiccup-find [:tbody :tr] content))))
        (is (=  0 (count (hf/hiccup-find [:#fleet-prev-page] content))))
        (is (=  1 (count (hf/hiccup-find [:#fleet-next-page] content))))))

    (testing "Should return us a list of 10 fleets with no next page link & a previous page link"
      (let [request {:user user :params {:page "2"}}
            {:keys [title content]} (fleet-handler/show-fleets request)]

        (is (= "List of fleets" title))

        (is (= 5 (count (hf/hiccup-find [:tbody :tr] content))))
        (is (=  1 (count (hf/hiccup-find [:#fleet-prev-page] content))))
        (is (=  0 (count (hf/hiccup-find [:#fleet-next-page] content))))))))
