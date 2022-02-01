(ns winter-onboarding-2021.fleet-management-service.handlers.fleet-test
  (:require [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [hiccup-find.core :as hf]
            [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.handlers.fleet :as fleet-handler]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management-service.db.fleet-test :as fleet-test-db]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

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

(defn- extract-and-str-ids [users]
  (mapv #(str (:users/id %)) users))

(deftest create-fleet
  (testing "Should create a fleet by an admin"
    (let [{admin-id :users/id :as admin} (factories/admin)
          {org-id :organisations/id} (factories/organisation {:organisations/created-by admin-id})
          request {:form-params {:name "Foo fleet"}
                   :user (assoc admin :users/org-id org-id)}
          response (fleet-handler/create-fleet request)
          inserted-fleet (first (db-core/query! ["SELECT * FROM FLEETS"]))]

      (is (= 302 (:status response)))
      (is (= #:fleets{:name "Foo fleet" :created-by admin-id}
             (select-keys inserted-fleet [:fleets/name :fleets/created-by])))
      (is (= {:success true
              :style-class "alert alert-success"
              :message "Fleet created successfully!"}
             (:flash response)))))

  (testing "Should not create a fleet if the admin doesn't belong to an organisation"
    (let [admin (factories/admin)
          request {:form-params {:name "Boo fleet"}
                   :user admin}
          response (fleet-handler/create-fleet request)]
      (is (= 302 (:status response)))
      (is (= {"Location" "/fleets/new"} (:headers response)))
      (is (= {:error true
              :style-class "alert alert-danger"
              :message "Could not create fleet, try again!"}
             (:flash response)))))

  (testing "Should not create a fleet even if the one of the managers or drivers dont belong to same org as admin"
    (let [{admin-id :users/id :as admin} (factories/admin)
          {org-id :organisations/id} (factories/organisation {:organisations/created-by admin-id})
          {org-id-2 :organisations/id} (factories/organisation {:organisations/created-by admin-id})
          managers (factories/create-list :users
                                          2
                                          (factories/overridden-generator {:users/org-id org-id
                                                                           :users/role "manager"}
                                                                          ::specs/users))
          drivers (factories/create-list :users
                                         1
                                         (factories/overridden-generator {:users/org-id org-id-2
                                                                          :users/role "driver"}
                                                                         ::specs/users))
          request {:form-params {:name "Foo fleet"
                                 :managers (extract-and-str-ids managers)
                                 :drivers (extract-and-str-ids drivers)}
                   :user (assoc admin :users/org-id org-id)}
          response (fleet-handler/create-fleet request)]

      (is (= 302 (:status response)))
      (is (= {"Location" "/fleets/new"} (:headers response)))
      (is (= {:error true
              :style-class "alert alert-danger"
              :message "Could not create fleet, try again!"} (:flash response))))))

(deftest create-fleet-with-1-manager
  (testing "Should create a fleet with 1 manager association & no drivers association"
    (let [{admin-id :users/id :as admin} (factories/admin)
          {org-id :organisations/id} (factories/organisation {:organisations/created-by admin-id})
          managers (factories/create-list :users
                                          1
                                          (factories/overridden-generator {:users/org-id org-id
                                                                           :users/role "manager"} ::specs/users))
          request {:form-params {:name "Foo fleet"
                                 :managers (first (extract-and-str-ids managers))}
                   :user (assoc admin :users/org-id org-id)}
          response (fleet-handler/create-fleet request)
          {fleet-id :fleets/id :as inserted-fleet} (first (db-core/query! ["SELECT * FROM FLEETS"]))]

      (is (= 302 (:status response)))
      (is (= #:fleets{:name "Foo fleet" :created-by admin-id}
             (select-keys inserted-fleet [:fleets/name :fleets/created-by])))
      (is (= {:success true
              :style-class "alert alert-success"
              :message "Fleet created successfully!"}
             (:flash response)))
      (is (= 2 (count (db-core/query! (sql/format (-> (h/select :*)
                                                      (h/from :users-fleets)
                                                      (h/where [:= :fleet-id fleet-id]))))))))))

(deftest create-fleet-with-3-manager
  (testing "Should create a fleet with 3 managers association & no drivers association"
    (let [{admin-id :users/id :as admin} (factories/admin)
          {org-id :organisations/id} (factories/organisation {:organisations/created-by admin-id})
          managers (factories/create-list :users
                                          3
                                          (factories/overridden-generator {:users/org-id org-id
                                                                           :users/role "manager"}
                                                                          ::specs/users))
          request {:form-params {:name "Foo fleet"
                                 :managers (extract-and-str-ids managers)}
                   :user (assoc admin :users/org-id org-id)}
          response (fleet-handler/create-fleet request)
          {fleet-id :fleets/id :as inserted-fleet} (first (db-core/query! ["SELECT * FROM FLEETS"]))]

      (is (= 302 (:status response)))
      (is (= #:fleets{:name "Foo fleet" :created-by admin-id}
             (select-keys inserted-fleet [:fleets/name :fleets/created-by])))
      (is (= {:success true
              :style-class "alert alert-success"
              :message "Fleet created successfully!"}
             (:flash response)))
      (is (= 4 (count (db-core/query! (sql/format (-> (h/select :*)
                                                      (h/from :users-fleets)
                                                      (h/where [:= :fleet-id fleet-id]))))))))))

(deftest create-fleet-with-3-manager-3-drivers
  (testing "Should create a fleet with 3 managers & 3 drivers associations"
    (let [{admin-id :users/id :as admin} (factories/admin)
          {org-id :organisations/id} (factories/organisation {:organisations/created-by admin-id})
          managers (factories/create-list :users
                                          3
                                          (factories/overridden-generator {:users/org-id org-id
                                                                           :users/role "manager"}
                                                                          ::specs/users))
          drivers (factories/create-list :users
                                         3
                                         (factories/overridden-generator {:users/org-id org-id
                                                                          :users/role "driver"}
                                                                         ::specs/users))
          request {:form-params {:name "Foo fleet"
                                 :managers (extract-and-str-ids managers)
                                 :drivers (extract-and-str-ids drivers)}
                   :user (assoc admin :users/org-id org-id)}
          response (fleet-handler/create-fleet request)
          {fleet-id :fleets/id :as inserted-fleet} (first (db-core/query! ["SELECT * FROM FLEETS"]))]

      (is (= 302 (:status response)))
      (is (= #:fleets{:name "Foo fleet" :created-by admin-id}
             (select-keys inserted-fleet [:fleets/name :fleets/created-by])))
      (is (= {:success true
              :style-class "alert alert-success"
              :message "Fleet created successfully!"}
             (:flash response)))
      (is (= 7 (count (db-core/query! (sql/format (-> (h/select :*)
                                                      (h/from :users-fleets)
                                                      (h/where [:= :fleet-id fleet-id]))))))))))

(deftest create-fleet-with-1-manager-1-drivers
  (testing "Should create a fleet with 1 manager & 1 driver associations"
    (let [{admin-id :users/id :as admin} (factories/admin)
          {org-id :organisations/id} (factories/organisation {:organisations/created-by admin-id})
          managers (factories/create-list :users
                                          1
                                          (factories/overridden-generator {:users/org-id org-id
                                                                           :users/role "manager"}
                                                                          ::specs/users))
          drivers (factories/create-list :users
                                         1
                                         (factories/overridden-generator {:users/org-id org-id
                                                                          :users/role "driver"}
                                                                         ::specs/users))
          request {:form-params {:name "Foo fleet"
                                 :managers (first (extract-and-str-ids managers))
                                 :drivers (first (extract-and-str-ids drivers))}
                   :user (assoc admin :users/org-id org-id)}
          response (fleet-handler/create-fleet request)
          {fleet-id :fleets/id :as inserted-fleet} (first (db-core/query! ["SELECT * FROM FLEETS"]))]

      (is (= 302 (:status response)))
      (is (= #:fleets{:name "Foo fleet" :created-by admin-id}
             (select-keys inserted-fleet [:fleets/name :fleets/created-by])))
      (is (= {:success true
              :style-class "alert alert-success"
              :message "Fleet created successfully!"}
             (:flash response)))
      (is (= 3 (count (db-core/query! (sql/format (-> (h/select :*)
                                                      (h/from :users-fleets)
                                                      (h/where [:= :fleet-id fleet-id]))))))))))
