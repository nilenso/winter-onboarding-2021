(ns winter-onboarding-2021.fleet-management-service.models.invite
  (:require [winter-onboarding-2021.fleet-management-service.db.invite :as invite]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]
            [winter-onboarding-2021.fleet-management-service.specs :as specs]
            [winter-onboarding-2021.fleet-management-service.error :as errors])
  (:import [org.postgresql.util PSQLException]))

(defn- token []
  (utils/rand-str 8))

(defn create [invite]
  (let [valid-invite (utils/select-keys-from-spec
                      (assoc invite
                             :invites/token (token)
                             :invites/status "active")
                      ::specs/invites-create-model)]
    (if (empty? valid-invite)
      errors/no-valid-keys
      (try (invite/create valid-invite)
           (catch PSQLException _
             errors/generic-error)))))

(defn find-by-keys [key-map]
  (let [valid-keymap (utils/select-keys-from-spec key-map ::specs/invites-create-model)]
    (cond (empty? valid-keymap)
          errors/no-valid-keys
          :else (invite/find-by-keys key-map))))
