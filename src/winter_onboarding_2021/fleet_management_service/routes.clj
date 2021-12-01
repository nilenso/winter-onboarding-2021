(ns winter-onboarding-2021.fleet-management-service.routes
  (:require [bidi.ring :as br]
            [ring.util.response :as response]
            [winter-onboarding-2021.fleet-management-service.handlers.cab :as cab]
            [winter-onboarding-2021.fleet-management-service.views.layout :as layout]
            [winter-onboarding-2021.fleet-management-service.handlers.core :as handler]))

(defn wrap-layout [handler title]
  (fn [request]
    (let [content (handler request)]
       (response/response (layout/application title content)))))

(def routes
  ["/" [["public" (br/->Resources {:prefix "/bootstrap"})]
        ["cabs" {:get {"" (wrap-layout cab/get-cabs "Cabs")}
                 :post {"/" cab/add}
                 "/new" (fn [_] {:status 200 :body "New cab form page"})}]
        ["healthcheck" handler/health-check]
        ["index" (wrap-layout handler/index "Homepage")]
        [true (fn [_] {:status 404 :body "Not found"})]]])
