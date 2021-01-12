(ns {{sanitized-name}}.routes.status
  (:require [corpus.responses :as responses]))

(def handle-status (constantly (responses/ok {:status "OK"})))
