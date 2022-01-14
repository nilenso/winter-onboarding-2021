(ns winter-onboarding-2021.fleet-management-service.views.cab
  (:require [winter-onboarding-2021.fleet-management-service.views.core :as core-views]
            [winter-onboarding-2021.fleet-management-service.utils :as utils]))

(defn cab-form [{:keys [name distance-travelled licence-plate]}]
  [:div {:id "content"}
   [:h1 {:class "text-success"} "This is the form page"]
   [:form {:action "/cabs" :method "POST"}
    [:div {:class "mb-3"}
     (core-views/labelled-text-input
      "Cab Name"
      :name "name"
      :required true
      :placeholder "Enter cab name"
      :value name)]
    [:div {:class "mb-3"}
     (core-views/labelled-text-input
      "Licence Plate"
      :required true
      :placeholder "Enter licence plate (only alphabets and number allowed)"
      :pattern "^[a-zA-Z0-9]*$"
      :value licence-plate)]
    [:div {:class "mb-3"}
     (core-views/labelled-text-input
      "Distance Travelled"
      :type "number"
      :required true
      :placeholder "Enter distance in meters"
      :value distance-travelled)]
    [:button {:type "submit" :class "btn btn-primary"} "Submit"]]])

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
                          {:data-bs-dismiss "modal", :type "button" :class "btn btn-secondary"} "Close"]
                         [:form {:action "/cabs/delete" :method "POST"}
                          [:input {:name "id" :value (:cabs/id cab) :hidden true}]
                          [:button {:type "submit" :class "btn btn-danger"} "Delete"]])})])

(def headers
  [{:label "Name" :value :cabs/name}
   {:label "Distance travelled" :value :cabs/distance-travelled}
   {:label "Licence plate" :value :cabs/licence-plate}
   {:label "Created at" :value :cabs/created-at}
   {:label "Updated at" :value :cabs/updated-at}])


(defn cab-row [row]
  [:tr
   [:td [:a {:href (str "/cabs/" (:cabs/id row))
             :class "link-primary"}
         (:cabs/name row)]]
   [:td (:cabs/distance-travelled row)]
   [:td (:cabs/licence-plate row)]
   [:td (utils/format-date (:cabs/updated-at row))]
   [:td (utils/format-date (:cabs/created-at row))]])

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
  (let [route (str "/cabs/" id)]
    [:div {:id "content"}
     [:h1 {:class "text-success"} "Update Cab"]
     [:form {:action route :method "POST"}
      [:div
       [:div {:class "mb-3"}
        (core-views/labelled-text-input
         "Cab Name"
         :placeholder "Enter cab name"
         :name "name"
         :required true
         :value name)]
       [:div {:class "mb-3"}
        (core-views/labelled-text-input
         "Licence Plate"
         :required true
         :placeholder "Enter licence plate (only alphabets and number allowed)"
         :pattern "^[a-zA-Z0-9]*$"
         :disabled true
         :value licence-plate)]
       [:div {:class "mb-3"}
        (core-views/labelled-text-input
         "Distance Travelled"
         :type "number"
         :placeholder "Enter distance in meters"
         :required true
         :value distance-travelled)]
       [:button {:type "submit" :class "btn btn-success"} "Update"]]]]))
