(ns {{sanitized-name}}-fixture
  (:require
    [{{sanitized-name}}.core :refer [->system app-cfg]]
    [piotr-yuxuan.closeable-map :refer [closeable-map]]
    [xiana.db :as db]
    [xiana.config :as config]))

(defn std-system-fixture
  [config f]
  (with-open [_ (->> (config/config config)
                     (merge app-cfg)
                     db/docker-postgres!
                     ->system
                     closeable-map)]
    (f)))
