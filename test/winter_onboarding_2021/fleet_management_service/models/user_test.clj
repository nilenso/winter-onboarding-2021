(ns winter-onboarding-2021.fleet-management-service.models.user-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [crypto.password.bcrypt :as password]
            [winter-onboarding-2021.fleet-management-service.factories :as factories]
            [winter-onboarding-2021.fleet-management-service.models.user :as user-model]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.db.core :as db-core]
            [winter-onboarding-2021.fleet-management-service.models.organisation :as org-models]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(defn select-relevant-keys [user]
  (select-keys user [:users/name :users/email :users/password :users/role]))

(deftest create-user
  (testing "Should create a user"
    (let [user #:users{:name "Harry Potter"
                       :role "admin"
                       :email "harry@hogwarts.edu"
                       :password "hermione@123"}
          _ (user-model/create user)]
      (is (= (select-relevant-keys user)
             (select-relevant-keys (first (user-model/find-by-keys
                                           {:users/email (:users/email user)}))))))))

(deftest user-with-invitation
  (testing "Should create an user with an invitation with org-id set to that of host")
  (let [db-admin (factories/admin)
        _ (org-models/create-and-associate db-core/db-conn {:organisations/name "foo-org"}
                                           db-admin)
        host (first (user-model/find-by-keys {:users/email (:users/email db-admin)}))
        invite (factories/invite-manager {:invites/created-by (:users/id db-admin)})
        new-user (factories/user {:users/role (:invites/role invite)
                                  :users/invite-id (:invites/id invite)
                                  :users/org-id (:users/org-id host)})
        _ (user-model/create new-user)
        db-user (user-model/find-by-keys {:users/email (:users/email new-user)})]
    (is (= (:invites/id invite)
           (:users/invite-id (first db-user))))
    (is (= (:users/org-id host)
           (:users/org-id (first db-user))))))

(deftest find-by-keys
  (testing "Should return a user given a key-map(properties)"
    (let [user (factories/admin)]
      (is (= (select-relevant-keys user)
             (select-relevant-keys (first (user-model/find-by-keys
                                           {:users/email (:users/email user)}))))))))

(deftest authenticate
  (testing "Correct login credentials, should return us user data"
    (let [user {:users/name "Harry Potter"
                :users/role "admin"
                :users/email "harry@hogwarts.edu"
                :users/password (password/encrypt "hermione@123")}
          created-user (user-model/create user)]

      (is (= {:found? true :user (dissoc created-user :users/password)}
             (dissoc (user-model/authenticate {:users/email (:users/email user)
                                               :users/password "hermione@123"})
                     :users/password)))))

  (testing "No email exists in the database"
    (is  (= {:found? false :user nil}
            (dissoc (user-model/authenticate {:users/email "foo@bar.com"
                                              :users/password "hermione@123"})
                    :users/password))))

  (testing "Password is wrong but the account is there"
    (is (= {:found? true :user nil}
           (dissoc (user-model/authenticate {:users/email "harry@hogwarts.edu"
                                             :users/password "wrongpassword"})
                   :users/password)))))

(deftest add-to-org
  (testing "Should add org-id to an user"
    (let [admin (factories/admin)
          org (org-models/create db-core/db-conn {:name "org-1" :created-by (:users/id admin)})
          _ (user-model/add-to-org db-core/db-conn org admin)

          db-admin (first (user-model/find-by-keys {:users/id (:users/id admin)}))]

      (is (= (:organisations/id org) (:users/org-id db-admin))))))
