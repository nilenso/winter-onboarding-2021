(ns user
  (:require [mount.core :as mount]
            [clojure.tools.namespace.repl :refer [refresh]]))

(defn restart-server []
  (mount/stop)
  (refresh :after 'mount/start))
