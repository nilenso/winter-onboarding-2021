(ns winter-onboarding-2021.fleet-management-service.views.cab
  (:require [winter-onboarding-2021.fleet-management-service.components.form :as component]))

(defn cab-form [{:keys [name distance-travelled licence-plate]}]
  (let [form (component/form {:action "/cabs"
                              :method "POST"
                              :enctype "multipart/form-data"
                              :inputs [{:label "Cab Name"
                                        :type "text"
                                        :name "name"
                                        :id "name"
                                        :required true
                                        :placeholder "Enter cab name"
                                        :value name}
                                       {:label "Licence Plate"
                                        :type "text"
                                        :name "licence-plate"
                                        :id "licence-plate"
                                        :required true
                                        :placeholder "Enter licence plate (only alphabets and number allowed)"
                                        :pattern "^[a-zA-Z0-9]*$"
                                        :value licence-plate}
                                       {:label "Distance Travelled"
                                        :type "number"
                                        :name "distance-travelled"
                                        :id "distance-travelled"
                                        :required true
                                        :placeholder "Enter distance in meters"
                                        :value distance-travelled}
                                       {:type "submit"
                                        :class "btn btn-primary"
                                        :value "Submit"}]})]
    [:div {:id "content"}
     [:h1 {:class "text-success"} "This is the form page"]
     form]))



(defn modal [data]
  [:div {:id (:modal-id data)
         :class "modal fade"
         :aria-hidden "true"
         :tabindex "-1"}
   [:div {:class "modal-dialog"}
    [:div {:class "modal-content"}
     [:div {:class "modal-header"}
      [:h5 {:class "modal-title"} (:title data)]
      [:button
       {:class "btn-close"
        :aria-label "Close"
        :data-bs-dismiss "modal"
        :type "button"}]]
     [:div {:class "modal-body"} (:body data)]
     [:div {:class "modal-footer"}
      (:footer data)]]]])

(defn cab [cab]
  [:div {:id "content" :class "p-5"}
   [:h2 (:cabs/name cab)]
   [:div {:class "mt-5"}
    [:div "Licence Plate"]
    [:h3 (:cabs/licence-plate cab)]]
   [:div {:class "mt-5"}
    [:div "Distance Travlled"]
    [:h3 (:cabs/distance-travelled cab)]]
   [:a {:id "cab-next-page" :class "btn btn-primary me-2"
        :href (str "/cabs/" (:cabs/id cab) "/edit")}
    "Update"]
   [:button.btn.btn-danger
    {:data-bs-target "#deleteCabModal"
     :data-bs-toggle "modal"
     :type "button"}
    "Delete"]
   (modal {:modal-id "deleteCabModal"
           :title (str "Delete " (:cabs/name cab) "?")
           :body "Once the cab is deleted, it can't be recovered."
           :footer (list [:button
                          {:data-bs-dismiss "modal", :type "button" :class "btn btn-secondary mt-3"} "Close"]
                         (component/form {:action "/cabs/delete"
                                          :method "POST"
                                          :inputs [{:name "id"
                                                    :value (:cabs/id cab)
                                                    :hidden true}
                                                   {:type "submit"
                                                    :class "btn btn-danger"
                                                    :value "Delete"}]}))})])

(def headers
  [{:label "Name" :value :cabs/name}
   {:label "Distance travelled" :value :cabs/distance-travelled}
   {:label "Licence plate" :value :cabs/licence-plate}
   {:label "Created at" :value :cabs/created-at}
   {:label "Updated at" :value :cabs/updated-at}])

(defn format-date [date]
  (.format (java.text.SimpleDateFormat. "MM/dd/yyyy HH:mm") date))

(defn cab-row [row]
  [:tr
   [:td [:a {:href (str "/cabs/" (:cabs/id row))
             :class "link-primary"}
         (:cabs/name row)]]
   [:td (:cabs/distance-travelled row)]
   [:td (:cabs/licence-plate row)]
   [:td (format-date (:cabs/updated-at row))]
   [:td (format-date (:cabs/created-at row))]])

(defn cab-not-found []
  [:div {:id "content" :class "p-5"}
   [:h2 "No cabs found"]])

(defn show-cabs [cabs page-num show-next-page?]
  (let [next-page-query (str "?page=" (inc page-num))]
    [:div
     [:table {:class "table table-primary min-vh-40"}
      [:thead [:tr (map (fn [header] [:th (:label header)])
                        headers)]]
      [:tbody (map cab-row cabs)]]
     [:div {:class "text-end"}
      (if show-next-page?
        [:a {:id "cab-next-page" :href next-page-query}  "Next Page > "] ())]]))

(defn update-cab-form [{:cabs/keys [id name licence-plate distance-travelled]}]
  (let [route (str "/cabs/" id)
        form (component/form {:action route
                              :method "POST"
                              :enctype "multipart/form-data"
                              :inputs [{:label "Cab Name"
                                        :type "text"
                                        :name "name"
                                        :id "name"
                                        :required true
                                        :placeholder "Enter cab name"
                                        :value name}
                                       {:label "Licence Plate"
                                        :type "text"
                                        :name "licence-plate"
                                        :id "licence-plate"
                                        :required true
                                        :placeholder "Enter licence plate (only alphabets and number allowed)"
                                        :pattern "^[a-zA-Z0-9]*$"
                                        :value licence-plate}
                                       {:label "Distance Travelled"
                                        :type "number"
                                        :name "distance-travelled"
                                        :id "distance-travelled"
                                        :required true
                                        :placeholder "Enter distance in meters"
                                        :value distance-travelled}
                                       {:type "submit"
                                        :class "btn btn-success"
                                        :value "Update"}]})]
    [:div {:id "content"}
     [:h1 {:class "text-success"} "Update Cab"]
     form]))
