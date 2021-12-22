(ns winter-onboarding-2021.fleet-management-service.migration-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.migration :as migration]))

(def calls (atom {}))

(use-fixtures :each
  (fn [f]
    (reset! calls {})
    (f)))
(use-fixtures :once fixtures/config)

(deftest migrations
  (testing "Should run migrations"
    (let [expected-config {:store :database
                           :db (:db-spec config/config)}
          stub (fn [command-name]
                 (fn [config & args]
                   (swap! calls  #(assoc %
                                         command-name
                                         (conj (get % command-name)
                                               [config args])))))]
      (testing "Run migrate"
        (with-redefs [migration/migratus-commands
                      {:migrate (stub :migrate)}]
          (migration/run-migratus "migrate")
          (is (= 1 (count (:migrate @calls))))
          (is (= [expected-config nil]
                 (-> @calls :migrate first)))))

      (testing "Run rollback"
        (with-redefs [migration/migratus-commands
                      {:rollback (stub :rollback)}]
          (migration/run-migratus "rollback")
          (is (= 1 (count (:rollback @calls))))
          (is (= [expected-config nil]
                 (-> @calls :rollback first)))))

      (testing "Run an `up` migration"
        (with-redefs [migration/migratus-commands
                      {:up (stub :up)}]
          (migration/run-migratus "up" "00001-foo-migration-up.sql")
          (is (= 1 (count (:up @calls))))
          (is (= [expected-config ["00001-foo-migration-up.sql"]]
                 (-> @calls :up first)))))

      (testing "Run a `down` migration"
        (with-redefs [migration/migratus-commands
                      {:down (stub :down)}]
          (migration/run-migratus "down" "00001-foo-migration-down.sql")
          (is (= 1 (count (:down @calls))))
          (is (= [expected-config ["00001-foo-migration-down.sql"]]
                 (-> @calls :down first)))))

      (testing "Run a `create` migration"
        (with-redefs [migration/migratus-commands
                      {:create (stub :create)}]
          (migration/run-migratus "create" "foo-migration")
          (is (= 1 (count (:create @calls))))
          (is (= [expected-config ["foo-migration"]]
                 (-> @calls :create first))))))))
