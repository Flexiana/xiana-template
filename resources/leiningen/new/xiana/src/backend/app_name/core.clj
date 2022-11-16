(ns {{sanitized-name}}.core
  (:require
    [{{sanitized-name}}.controllers.index :as index]
    [{{sanitized-name}}.controllers.re-frame :as re-frame]
    [{{sanitized-name}}.controllers.swagger :as swagger]
    [xiana.config :as config]
    [xiana.db :as db]
    [xiana.interceptor :as interceptors]
    [xiana.rbac :as rbac]
    [xiana.route :as routes]
    [xiana.swagger :as xsw]
    [xiana.session :as session]
    [xiana.webserver :as ws]
    [reitit.ring :as ring]
    [clojure.walk]
    [ring.util.response]
    [reitit.coercion.malli]
    [malli.util :as mu]
    [reitit.swagger :as sswagger]
    [xiana.commons :refer [rename-key]]))

(def routes
  [["/"               {:action #'index/handle-index
                       :swagger {:produces ["text/html"]}}]
   ["/re-frame"       {:action #'re-frame/handle-index
                       :swagger {:produces ["text/html"]}}]
   ["/assets/*"       (ring/create-resource-handler {:path "/"})]])

(defn ->system
  [app-cfg]
  (-> (config/config app-cfg)
      (rename-key :framework.app/auth :auth)
      xsw/->swagger-data
      routes/reset
      rbac/init
      session/init-backend
      db/connect
      db/migrate!
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
