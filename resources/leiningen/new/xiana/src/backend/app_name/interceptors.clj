(ns {{sanitized-name}}.interceptors
  (:require
    [xiana.core :as xiana]))

(def sample-{{sanitized-name}}-controller-interceptor
  {:enter (fn [{request :request {:keys [handler controller match]} :request-data :as state}]
            (xiana/ok state))
   :leave (fn [{response :response :as state}]
            (xiana/ok state))})
