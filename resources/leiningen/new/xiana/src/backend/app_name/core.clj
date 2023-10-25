(ns {{sanitized-name}}.core
  (:require
    [{{sanitized-name}}.controllers.index :as index]
    [{{sanitized-name}}.controllers.re-frame :as re-frame]
    [xiana.config :as config]
    [xiana.db :as db]
    [xiana.interceptor :as interceptors]
    [xiana.interceptor.error :as error]
    [xiana.rbac :as rbac]
    [xiana.route :as routes]
    [xiana.session :as session]
    [xiana.webserver :as ws]
    [xiana.swagger :as xsw]
    [reitit.ring :as ring]
    [xiana.commons :refer [rename-key]]))

(def routes
  [["/"               {:get {:action #'index/handle-index}}]
   ["/re-frame"       {:no-doc true
                       :action #'re-frame/handle-index}]
   ^:no-doc ["/assets/*"  (ring/create-resource-handler {:path "/"})]])

(defn ->system
  [app-cfg]
  (-> (config/config app-cfg)
      (rename-key :xiana/auth :auth)
      xsw/add-swagger-endpoints
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
                             error/response
                             session/guest-session-interceptor
                             interceptors/view
                             interceptors/side-effect
                             db/db-access
                             rbac/interceptor]})

(defn -main
  [& _args]
  (->system app-cfg))
