(ns winter-onboarding-2021.fleet-management-service.handlers.invite-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.fixtures :as fixtures]
            [winter-onboarding-2021.fleet-management-service.handlers.invite :as invite]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.db.invite :as invite-db]
            [winter-onboarding-2021.fleet-management-service.factories :as factory]
            [hiccup-find.core :as hf]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)

(deftest create-invite
  (testing "Should create a new invite and return invite link"
    (let [user (factory/admin)
          resp (invite/create {:user user
                                :form-params {:role "manager"
                                              :valid-until "2022-01-29"
                                              :usage-limit "10"}
                                :headers {"host" "https://foober.com"}})
          db-resp (invite-db/find-by-keys {:invites/created-by (:users/id user)})]
      (is (= 302
             302))
      (is (= (str "Invite created successfully. <br> Link - <a href=https://foober.com/users/signup?token="
                  (:invites/token (first db-resp))
                  ">"
                  "https://foober.com/users/signup?token="
                  (:invites/token (first db-resp))
                  "</a>") 
             (get-in resp [:flash :message])))))
  (testing "Should return Invalid parameters flash message when incorrect data is sent"
    (let [user (factory/admin)
          resp (invite/create {:user user
                                :form-params {:role "owner"
                                              :valid-until "2022-01-29"
                                              :usage-limit "10"}
                                :headers {"host" "https://foober.com"}})]
      (is (= 302
             302))
      (is (= "Invalid parameters sent, try again"
             (get-in resp [:flash :message])))))
  (testing "Should NOT create invite when user is not loggeed in"
    (let [resp (invite/create {:form-params {:role "owner"
                                              :valid-until "2022-01-29"
                                              :usage-limit "10"}
                                :headers {"host" "https://foober.com"}})]
      (is (= 302
             302))
      (is (= "Invalid parameters sent, try again"
             (get-in resp [:flash :message]))))))

(deftest get-invites-table
  (testing "Should return table of invites"
    (let [user (factory/admin)
          user-id (:users/id user)
          invites (take 10 (repeatedly factory/invite))
          request-list (map (fn [x]
                              {:form-params (dissoc x :invites/created-by)
                               :user {:users/id user-id}})
                            invites)
          _ (doall (map invite/create request-list))
          invites-page (invite/invites-page {:user user})]
      (is (= 10
             (count (hf/hiccup-find [:tbody :tr] (:content invites-page)))))
      (is (= '([:h2 "No invites found"])
             (hf/hiccup-find [:h2] (:content (invite/invites-page {:user {:users/id (utils/uuid)}})))))))
  
  (testing "Should return table of invites"
    (let [invite (factory/invite)
          request {:form-params (dissoc invite :invites/created-by)}
          resp (invite/invites-page request)]
      (is (= {:error :user-not-found-in-request}
             resp)))))
