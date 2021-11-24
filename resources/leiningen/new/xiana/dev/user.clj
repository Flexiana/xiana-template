(ns user
  (:gen-class)
  (:require
    [{{sanitized-name}}.core :refer [->system app-cfg]]
    [clojure.tools.logging :refer [*tx-agent-levels*]]
    [clojure.tools.namespace.repl :refer [refresh]]
    [shadow.cljs.devtools.api :as shadow.api]
    [shadow.cljs.devtools.server :as shadow.server]))

(alter-var-root #'*tx-agent-levels* conj :debug :trace)

(defonce dev-sys (atom {}))

(def dev-app-config
  app-cfg)

(defn- stop-dev-system
  []
  (when (:webserver @dev-sys) (.close @dev-sys))
  (reset! dev-sys {}))

(defn- start-dev-system
  []
  (stop-dev-system)
  (refresh)
  (shadow.server/start!)
  (shadow.api/watch :app)
  (reset! dev-sys (->system dev-app-config)))

(comment
  (start-dev-system)
  (stop-dev-system))