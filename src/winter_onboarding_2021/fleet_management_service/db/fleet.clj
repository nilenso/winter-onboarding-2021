(ns winter-onboarding-2021.fleet-management-service.db.fleet
  (:require [clojure.spec.alpha :as s]
            [honey.sql :as sql]
            [honey.sql.helpers :refer [select from limit offset order-by]]
            [winter-onboarding-2021.fleet-management-service.error :as error]
            [winter-onboarding-2021.fleet-management-service.db.core :as db]
            [winter-onboarding-2021.fleet-management-service.specs :as spec]))

(defn create [fleet]
  (if (s/valid? ::spec/fleets fleet)
    (db/insert! :fleets fleet)
    (let [error-msg (s/explain-str ::spec/fleets fleet)]
      (assoc error/validation-failed :error-msg error-msg))))

(defn select! [off lim]
  (db/query! (sql/format (-> (select :id
                                     :name
                                     :created-by
                                     :created-at)
                             (from :fleets)
                             (limit lim)
                             (offset off)
                             (order-by :created-at)))))
