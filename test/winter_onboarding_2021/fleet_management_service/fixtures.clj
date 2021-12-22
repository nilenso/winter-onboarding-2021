(ns winter-onboarding-2021.fleet-management-service.fixtures
  (:require [next.jdbc :as jdbc]
            [mount.core :as mount]
            [aero.core :refer [read-config]]
            [winter-onboarding-2021.fleet-management-service.config :as config]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [clojure.java.io :as io]))

(defn config [f]
  (mount/stop #'config/config)
  (mount/start-with
   {#'config/config (read-config
                     (io/resource "config.edn")
                     {:profile :test})})
  (f)
  (mount/stop #'config/config))

(defn db-connection [f]
  (mount/stop #'db/db-conn)
  (mount/start #'db/db-conn)
  (f)
  (mount/stop #'db/db-conn))

(defn clear-db [f]
  (jdbc/execute! db/db-conn  ["TRUNCATE TABLE cabs, users CASCADE"])
  (f))

;; config doubt --> pass config via CLI?
