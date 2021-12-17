(ns winter-onboarding-2021.fleet-management-service.session
  (:require [ring.middleware.session.store :as store]
            [ring.middleware.session.memory :as ring-memory]
            [mount.core :as mount :refer [defstate]]))

(defonce all-sessions (atom {}))

(defstate session-store
  :start (ring-memory/memory-store all-sessions))

(defn write-session [session-id data]
  (store/write-session session-store session-id data))

(defn read-session [session-id]
  (store/read-session session-store session-id))

(defn delete-session [session-id]
  (store/delete-session session-store session-id))
