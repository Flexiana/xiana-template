(ns {{sanitized-name}}
  (:require
   [com.stuartsierra.component :as component]
   [controllers.index :as index]
   [controllers.re-frame :as re-frame]
   [framework.components.app.core :as xiana.app]
   [framework.components.router.core :as xiana.router]
   [framework.components.web-server.core :as xiana.web-server]
   [framework.config.core :as config]
   [framework.db.storage :as db.storage]
   [interceptors]
   [reitit.ring :as ring]))

(def routes
  [["/" {:controller index/handle-index}]
   ["/re-frame" {:controller re-frame/handle-index}]
   {{#workspaces?}}["/workspaces" {:controller re-frame/handle-workspaces}]{{/workspaces?}}
   ["/assets/*" (ring/create-resource-handler)]])

(defn system
  [config]
  (let [pg-cfg (:framework.db.storage/postgresql config)
        app-cfg (:framework.app/ring config)
        web-server-cfg (:framework.app/web-server config)]
    (->
      (component/system-map
        :config config
        :db (db.storage/postgresql pg-cfg)
        :router (xiana.router/make-router routes)
        :app (xiana.app/make-app app-cfg
                                 []
                                 [interceptors/sample-{{sanitized-name}}-controller-interceptor])
        :web-server (xiana.web-server/make-web-server web-server-cfg))
      (component/system-using
        {:router     [:db]
         :app        [:router :db]
         :web-server [:app]}))))

(defn -main
  [& _args]
  (let [config (config/edn)]
    (component/start (system config))))

(defonce st
  (-> (config/edn)
      system
      atom))

(defn- start-dev-system
  []
  (swap! st component/start))

(defn- stop-dev-system
  []
  (swap! st component/stop))
