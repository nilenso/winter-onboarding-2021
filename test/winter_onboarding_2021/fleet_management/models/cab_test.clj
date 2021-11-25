(ns winter-onboarding-2021.fleet-management.models.cab-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [winter-onboarding-2021.fleet-management-service.models.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as handler]
            [winter-onboarding-2021.fleet-management.fixtures :as fixtures])
  (:import [org.postgresql.util PSQLException]))

(use-fixtures :once fixtures/config fixtures/db-connection)
(use-fixtures :each fixtures/clear-db)



(deftest create-cab
  (testing "Should add a cab"
    (let [created-cab (cab/create {:licence_plate "HR20X 6710"
                                   :name "Maruti Celerio"
                                   :distance_travelled 2333})]
      (is (= #:cabs{:licence_plate "HR20X 6710"
                    :name "Maruti Celerio"
                    :distance_travelled 2333}
             (dissoc created-cab
                     :cabs/id
                     :cabs/created_at
                     :cabs/updated_at)))))

  (testing "Should fail to create a cab because of invalid cab"
    (is (thrown-with-msg?
         PSQLException
         #"null value in column \"licence_plate\" of relation \"cabs\""
         (cab/create {:name "Kia"})))))



(deftest cab-handler-test
  (testing "returns redered cabs list"))

;; (deftest list-cabs
;;   (testing "Returns a list of cabs"
;;     [let cabs-list (factory/create-cabs 3)
;;      (is (= cabs-list
;;             cab/select))]))

















;; (defn add-cab [cab]
;;   (cab/create cab))

;; (defn add-3-cabs [cabs]
;;   (map add-cab cabs))

;; (defn dissoc-metadata [cab]
;;   (dissoc cab
;;           :cabs/id
;;           :cabs/created_at
;;           :cabs/updated_at))

;; (defn get-dissociated-cabs-list [cabs]
;;   (map dissoc-metadata cabs))

;; (def cab-list (get-dissociated-cabs-list (cab/select)))


;; (def cabs [{:licence_plate "MH13AD6541"
;;             :name "Tata Harrier"
;;             :distance_travelled 1432}
;;            {:licence_plate "MH23AH1152"
;;             :name "Audi A6"
;;             :distance_travelled 1029}
;;            {:licence_plate "OD13AH3455"
;;             :name "Maruti Suzuki Ciaz"
;;             :distance_travelled 15666}])

;;GET request to /cabs
;;they will return cabs list


;;testing the handler
;;it will add the cabs //factories
;;it will get the cabs from db
;;it will wrap in a response
;;we check the data and the status if it 200



