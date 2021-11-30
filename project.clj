(defproject winter-onboarding-2021 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.9.4"]
                 [ring/ring-json "0.5.1"]
                 [ring-logger "1.0.1"]
                 [org.postgresql/postgresql "42.2.24.jre7"]
                 [bidi "2.1.6"]
                 [aero "1.1.6"]
                 [com.github.seancorfield/next.jdbc "1.2.737"]
                 [hiccup "1.0.5"]
                 [mount "0.1.16"]
                 [migratus "1.3.5"]]
  :aliases {"migrations" ["run" "-m" "winter-onboarding-2021.fleet-management-service.migration/run-migratus"]}
  :profiles {:uberjar {:aot :all}
             :test {:cloverage {:fail-threshold 50}}
             :dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "1.0.0"]]}}
  :plugins [[lein-cloverage "1.2.2"]]
  :repl-options {:init-ns winter-onboarding-2021.core}
  :resource-paths ["config"]
  :uberjar-name "winter-onboarding-2021-standalone.jar"
  :main winter-onboarding-2021.fleet-management-service.core
  :min-lein-version "2.0.0")
