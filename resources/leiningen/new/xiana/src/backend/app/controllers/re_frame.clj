(ns controllers.re-frame
  (:require [xiana.core :as xiana]
            [ring.util.response :as ring]))
{{#workspaces?}}
(defn handle-workspaces
  [state]
  (xiana/ok
    (assoc state
      :response
      (-> "workspaces.html"
          (ring/resource-response {:root "public"})
          (ring/header "Content-Type" "text/html; charset=utf-8"))))){{/workspaces?}}

(defn handle-index
  [state]
  (xiana/ok
   (assoc state
          :response
          (-> "index.html"
              (ring/resource-response {:root "public"})
              (ring/header "Content-Type" "text/html; charset=utf-8")))))
