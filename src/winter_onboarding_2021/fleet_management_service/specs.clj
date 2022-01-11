(ns winter-onboarding-2021.fleet-management-service.specs
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))

(defn str->long [str]
  (if (= (type str) java.lang.Long)
    str
    (try (Long/parseLong str)
         (catch Exception _ :clojure.spec.alpha/invalid))))

(defn- pos-int-gen []
  (gen/large-integer* {:min 0
                       :max 200000000}))

(def licence-plate-regex #"^[a-zA-Z0-9]*$")

(s/def :cabs/name string?)
(s/def :cabs/id uuid?)
(s/def :cabs/created-at string?)
(s/def :cabs/updated-at string?)
(s/def :cabs/licence-plate (s/and string? not-empty #(re-matches licence-plate-regex %)))
(s/def :cabs/distance-travelled (s/with-gen (s/conformer str->long)
                                  pos-int-gen))
(s/def ::cabs
  (s/keys :req [:cabs/name
                :cabs/licence-plate
                :cabs/distance-travelled]))

(s/def ::cabs-all-attr
  (s/keys :req [:cabs/name
                :cabs/licence-plate
                :cabs/distance-travelled
                :cabs/id
                :cabs/created-at
                :cabs/updated-at]))

(s/def ::cabs-update-form
  (s/keys :req [:cabs/name
                :cabs/distance-travelled]))

(def ^:private non-empty-alphanumeric-string
  (gen/such-that #(not= "" %)
                 (gen/string-alphanumeric)))

(def email-counter (atom 0))

; We used the email generator function from the below gist
; https://gist.github.com/conan/2edca210999b96ad26d38c1ee96dfe40#file-url-gen-clj
(defn- email-gen []
  (gen/fmap
   (fn [[name host tld]]
     (swap! email-counter inc)
     (str name "@" host "." @email-counter "." tld))
   (gen/tuple
    non-empty-alphanumeric-string
    non-empty-alphanumeric-string
    non-empty-alphanumeric-string)))

(s/def :users/name string?)
(s/def :users/email (s/with-gen (s/and string? #(re-matches #".+\@.+\..+" %))
                      email-gen))
(s/def :users/role #{"admin" "manager" "driver"})
(s/def :users/password (s/and string? not-empty))

(s/def ::users
  (s/keys :req [:users/name
                :users/role
                :users/email
                :users/password]))

(s/def ::users-all-attr
  (s/keys :req [:users/id
                :users/name
                :users/role
                :users/email
                :users/password]))

(s/def ::signup-form
  (s/keys :req [:users/name
                :users/email
                :users/role
                :users/password]))

(s/def ::login-params
  (s/keys :req [:users/email
                :users/password]))
                   
; Fleets
(s/def :fleets/name string?)
(s/def :fleets/created-by uuid?)

(s/def ::fleet-form
  (s/keys :req-un [:fleets/name
                   :fleets/created-by]))

; Pagination
(s/def ::pagination-params
  (s/keys :req-un [:pagination/limit
                   :pagination/offset]))

(s/def :pagination/offset (comp not neg-int?))
(s/def :pagination/limit (comp not neg-int?))
