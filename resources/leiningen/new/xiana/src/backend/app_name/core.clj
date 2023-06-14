(ns {{sanitized-name}}.core
  (:require
    [{{sanitized-name}}.controllers.index :as index]
    [{{sanitized-name}}.controllers.re-frame :as re-frame]
    [xiana.config :as config]
    [xiana.db :as db]
    [xiana.interceptor :as interceptors]
    [xiana.rbac :as rbac]
    [xiana.route :as routes]
    [xiana.session :as session]
    [xiana.webserver :as ws]
    [reitit.ring :as ring]
    [clojure.walk]
    [ring.util.response]
    [reitit.coercion.malli]
    [xiana.commons :refer [rename-key]]))

(def routes
  [["/"               {:action #'index/handle-index}]
   ["/re-frame"       {:action #'re-frame/handle-index}]
   ["/assets/*"       (ring/create-resource-handler {:path "/"})]])

(defn ->system
  [app-cfg]
  (-> (config/config app-cfg)
      (rename-key :xiana/auth :auth)
      routes/reset
      rbac/init
      db/connect
      db/migrate!
      session/init-backend
      ws/start))

(def app-cfg
  {:routes routes
   :router-interceptors     []
   :controller-interceptors [(interceptors/muuntaja)
                             interceptors/params
                             session/guest-session-interceptor
                             interceptors/view
                             interceptors/side-effect
                             db/db-access
                             rbac/interceptor]})

(defn -main
  [& _args]
  (->system app-cfg))
