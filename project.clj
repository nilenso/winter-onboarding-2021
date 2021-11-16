(defproject winter-onboarding-2021 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring/ring-core "1.9.4"]
                 [ring/ring-jetty-adapter "1.8.2"]
                 [bidi "2.1.6"]
                 [aero "1.1.6"]
                 ; [com.github.seancorfield/next.jdbc "1.2.737"]
                 [hiccup "1.0.5"]
                 [mount "0.1.16"]]
  :repl-options {:init-ns winter-onboarding-2021.core}
  :main winter-onboarding-2021.fleet-management-service.core)
