(ns user
  (:require [mount.core :as mount]
            [clojure.tools.namespace.repl :refer [refresh]]))

#_:clj-kondo/ignore
(defn restart-server []
  (mount/stop)
  (refresh :after 'mount/start))
