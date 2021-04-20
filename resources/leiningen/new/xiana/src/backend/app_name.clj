(ns {{sanitized-name}}
  (:require
    [com.stuartsierra.component :as component]
    [controllers.index :as index]
    [controllers.re-frame :as re-frame]
    [framework.components.app.core :as xiana.app]
    [framework.components.interceptors :as interceptors]
    [framework.components.router.core :as xiana.router]
    [framework.components.session.backend :as session-backend]
    [framework.components.web-server.core :as xiana.web-server]
    [framework.config.core :as config]
    [framework.db.storage :as db.storage]
    [nrepl.server :refer [start-server stop-server]]
    [reitit.ring :as ring]))

(def routes
  [["/" {:controller index/handle-index}]
   ["/re-frame" {:controller re-frame/handle-index}]
   ["/assets/*" (ring/create-resource-handler)]])

(defn system
  [config]
  (let [acl-cfg (select-keys config [:acl/permissions :acl/roles])
        session-bcknd (session-backend/init-in-memory-session)]
    {:db         (db.storage/->postgresql config)
     :router     (xiana.router/->router config routes)
     :app        (xiana.app/->app {:acl-cfg                 acl-cfg
                                   :session-backend         session-bcknd
                                   :router-interceptors     []
                                   :controller-interceptors [(interceptors/muuntaja)
                                                             interceptors/params
                                                             (interceptors/require-logged-in)
                                                             interceptors/session-interceptor
                                                             interceptors/view
                                                             interceptors/side-effect
                                                             (interceptors/db-access)
                                                             ;(interceptors/acl-restrict {:prefix "/api"})
                                                             ]})

     :web-server (xiana.web-server/->web-server config)}))

(def sys-deps
  {:router     [:db]
   :app        [:router :db]
   :web-server [:app]})

(defonce st (atom {}))

(defn -main
  [& _args]
  (reset! st (component/start (-> (config/edn)
                                  system
                                  component/map->SystemMap
                                  (component/system-using sys-deps)))))
