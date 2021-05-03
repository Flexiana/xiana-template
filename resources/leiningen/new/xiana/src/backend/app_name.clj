(ns {{sanitized-name}}
  (:require
    [com.stuartsierra.component :as component]
    [controllers.index :as index]
    [controllers.re-frame :as re-frame]
    [framework.components.interceptors :as interceptors]
    [framework.components.session.backend :as session-backend]
    [framework.components.web-server.core :as xiana.web-server]
    [framework.config.core :as config]
    [framework.db.storage :as db.storage]
    [reitit.ring :as ring]))

(def routes
  [["/" {:action index/handle-index}]
   ["/re-frame" {:action re-frame/handle-index}]
   ["/assets/*" (ring/create-resource-handler {:path "/"})]])

(def app-config
  (let [config (config/edn)]
    {:acl-cfg                 (select-keys config [:acl/permissions :acl/roles])
     :auth                    (:framework.app/auth config)
     :session-backend         (session-backend/init-in-memory-session)
     :router-interceptors     []
     :controller-interceptors [ ;interceptors/log
                               (interceptors/muuntaja)
                               interceptors/params
                               ;(interceptors/require-logged-in)
                               interceptors/session-interceptor
                               ;interceptors/view
                               interceptors/side-effect
                               (interceptors/db-access)
                               ;(interceptors/acl-restrict {:prefix "/api"})
                               ]}))

(defn system
  [config app-config routes]
  {:db         (db.storage/->postgresql config)
   :web-server (xiana.web-server/->web-server config app-config routes)})

(def sys-deps
  {:web-server [:db]})

(defonce st (atom {}))

(defn -main
  [& _args]
  (reset! st (component/start (-> (config/edn)
                                  (system app-config routes)
                                  component/map->SystemMap
                                  (component/system-using sys-deps)))))
