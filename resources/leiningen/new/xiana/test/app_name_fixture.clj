(ns {{sanitized-name}}-fixture
  (:require
    [{{sanitized-name}}.core :refer [->system app-cfg]]))

(defn std-system-fixture
  [config f]
  (with-open [_ (->system (merge app-cfg config))]
    (f)))

