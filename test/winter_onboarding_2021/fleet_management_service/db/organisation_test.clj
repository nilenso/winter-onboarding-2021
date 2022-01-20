(ns winter-onboarding-2021.fleet-management-service.db.organisation-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.db.organisation :as org-db]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)


(deftest create
  (testing "Should create an organisation in DB"
    (let [admin (factories/admin)
          admin-id (:users/id admin)
          org (gen/generate (gen/fmap #(assoc % :organisations/created-by admin-id)
                                      (s/gen ::specs/organisations)))
          _ (org-db/create org)
          db-org (first (db-core/query! ["SELECT * FROM organisations;"]))]
      (is (= org (select-keys db-org
                              [:organisations/name
                               :organisations/created-by])))))

  (testing "Should not create an organsiation in DB if supplied data is not namespaced"
    (let [admin (factories/admin)
          org  {:name "unqualified-keys"
                :created-by (:users/id admin)}]

      (org-db/create org)
      (is (empty? (db-core/find-by-keys! :organisations
                                         #:organisations{:name "unqualified-keys"})))))

  (testing "Should not create an organsiation in DB if org name is empty"
    (let [admin (factories/admin)
          org  #:organisations{:name ""
                               :created-by (:users/id admin)}]

      (org-db/create org)
      (is (empty? (db-core/find-by-keys! :organisations
                                         #:organisations{:name ""
                                                         :created-by (:users/id admin)}))))))
