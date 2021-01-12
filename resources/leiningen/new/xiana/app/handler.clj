(ns {{sanitized-name}}.handler
  (:require [corpus.router.reitit :as corpus]
            [integrant.core :as ig]
            [{{sanitized-name}}.controllers.task :as task]
            [{{sanitized-name}}.routes.status :as status]
            [corpus.responses.hiccup :as hiccup]))

(def controllers
  [task/controller])

(defn make-routes
  [db]
  (concat [""
           ["/status" {:get status/handle-status}]]
          ;; TODO: make it more clear
          (map (partial corpus/compose-route db) controllers)))

(defmethod ig/init-key ::ring
  [_ {:keys [ring-conf db]}]
  (corpus/ring-handler ring-conf
                       (corpus/router (make-routes db) ring-conf hiccup/wrap-render)))
