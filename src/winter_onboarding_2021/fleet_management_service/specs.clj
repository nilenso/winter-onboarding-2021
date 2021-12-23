(ns winter-onboarding-2021.fleet-management-service.specs
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))

(def licence-plate-regex #"^[a-zA-Z0-9]*$")

(s/def :cabs/name string?)
(s/def :cabs/licence-plate (s/and string? not-empty #(re-matches licence-plate-regex %)))
(s/def :cabs/distance-travelled (s/int-in 0 100000000000))
(s/def :cabs-form/distance-travelled (s/conformer
                                      #(try (Long/parseLong %)
                                            (catch Exception _ :clojure.spec.alpha/invalid))
                                      str))

(s/def ::create-cab-form
  (s/keys :req-un [:cabs/name
                   :cabs/licence-plate
                   :cabs-form/distance-travelled]))

(s/def ::update-cab-form
  (s/keys :req-un [:cabs/name
                   :cabs-form/distance-travelled]))

(s/def ::cabs
  (s/keys :req [:cabs/name
                :cabs/licence-plate
                :cabs/distance-travelled]))

;; Users

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
(s/def :users/email (s/with-gen (and string? #(re-matches #".+\@.+\..+" %))
                      email-gen))
(s/def :users/role #{"admin" "fleet-manager" "driver"})
(s/def :users/password (s/and string? not-empty))

(s/def ::users
  (s/keys :req [:users/name
                :users/role
                :users/email
                :users/password]))

(s/def ::signup-form 
       (s/keys :req-un [:users/name
                        :users/email
                        :users/role
                        :users/password]))

(s/def ::login-params
  (s/keys :req-un [:users/email
                   :users/password]))
                   
