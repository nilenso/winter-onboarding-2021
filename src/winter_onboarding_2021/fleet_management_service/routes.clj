(ns winter-onboarding-2021.fleet-management-service.routes
  (:require [bidi.ring :as br]
            [ring.util.response :as response]
            [ring.middleware.json :refer [wrap-json-response]]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]
            [winter-onboarding-2021.fleet-management-service.handlers.core :as handler]))

(defn wrap-layout [handler title]
  (fn [request]
    (let [content (handler request)]
       (response/response (layout/application request title content)))))

(def routes
  ["/" [["public" {:get (br/->Resources {:prefix "/bootstrap"})}]
        ["cabs" [["" {:get (wrap-layout cab/get-cabs "Cabs")
                      :post cab/create}]
                 ["/new" {:get (wrap-layout cab/new "Add new cab")}]]]
        ["healthcheck" {:get (wrap-json-response handler/health-check)}]
        ["index" {:get handler/index}]
        [true (fn [_] {:status 404 :body "Not found"})]]])


