(ns winter-onboarding-2021.fleet-management-service.routes
  (:require [bidi.ring :as br]
            [ring.util.response :as response]
            [ring.middleware.json :refer [wrap-json-response]]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as handlers]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]
            [winter-onboarding-2021.fleet-management-service.handlers.core :as handler]))

(defn wrap-layout [handler]
  (fn [request]
    (let [data (handler request)]
      (response/response (layout/application
                          request
                          (:title data)
                          (:content data))))))

(def routes
  ["/" [["public" {:get (br/->Resources {:prefix "/bootstrap"})}]
        ["cabs" {"" {:get (wrap-layout handlers/get-cabs)
                     :post handlers/create}
                 "/new" {:get (wrap-layout handlers/new)}
                 ["/" :id] {:get (wrap-layout handlers/view-cab)}}]
        ["healthcheck" {:get (wrap-json-response handler/health-check)}]
        ["index" {:get (wrap-layout handler/index)}]
        [true (fn [_] {:status 404 :body "Not found"})]]])
