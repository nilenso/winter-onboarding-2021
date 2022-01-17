(ns winter-onboarding-2021.fleet-management-service.handlers.invite-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.handlers.invites :as invites]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.db.user :as user-db]
            [winter-onboarding-2021.fleet-management-service.db.invite :as invite-db]
            [winter-onboarding-2021.fleet-management-service.factories :as factory]
            [hiccup-find.core :as hf]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-invite
  (testing "Should create a new invite and return invite link"
    (let [user (user-db/create (factory/admin))
          resp (invites/create {:user user
                                :form-params {:role "manager"
                                              :valid-until "2022-01-29"
                                              :usage-limit "10"}})
          db-resp (invite-db/find-by-keys {:invites/created-by (:users/id user)})]
      (is (= 302
             302))
      (is (= (str "Invite created successfully. <br> Link - http://localhost:3000/users/signup?token=" (:invites/token (first db-resp))) 
             (get-in resp [:flash :message]))))))

(deftest get-invites-table
  (testing "Should return table of invites"
    (let [user (user-db/create (factory/admin))
          user-id (:users/id user)
          invites (take 10 (repeatedly factory/invite))
          request-list (map (fn [x]
                              {:form-params (utils/remove-namespace (dissoc x :invites/created-by))
                               :user {:users/id user-id}})
                            invites)
          _ (doall (map invites/create request-list))
          invites-page (invites/invites-page {:user user})]
      (is (= 10
             (count (hf/hiccup-find [:tbody :tr] (:content invites-page))))))))
