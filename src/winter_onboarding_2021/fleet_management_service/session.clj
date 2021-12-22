(ns winter-onboarding-2021.fleet-management-service.session
  (:require [ring.middleware.session.store :as store]
            [ring.middleware.session.memory :as ring-memory]
            [mount.core :as mount :refer [defstate]]))

(defonce all-sessions (atom {}))

(defstate store
  :start (ring-memory/memory-store all-sessions))

(defn write [session-id data]
  (store/write-session store session-id data))

(defn lookup [session-id]
  (store/read-session store session-id))

(defn delete [session-id]
  (store/delete-session store session-id))
