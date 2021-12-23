(ns winter-onboarding-2021.fleet-management-service.components.form)

(defn input [attributes]
  (let [options (dissoc attributes :label) 
        label (:label attributes)
        id (:id attributes)]
    [:div {:class "mt-3"}
     (when label
       [:label {:for id :class "form-label"} label])
     [:input (merge {:class "form-control"}
                    options)]]))

(defn form [attributes]
  (let [form-attr (merge nil (select-keys attributes [:action :method :enctype]))
        inputs (when (:inputs attributes)
                 (map input (:inputs attributes)))]
    [:form form-attr inputs]))
