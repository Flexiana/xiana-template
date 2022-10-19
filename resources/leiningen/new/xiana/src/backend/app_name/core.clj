(ns {{sanitized-name}}.core
  (:require
    [{{sanitized-name}}.controllers.index :as index]
    [{{sanitized-name}}.controllers.re-frame :as re-frame]
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
    [xiana.commons :refer [rename-key]]))

(def routes
  [["/" {:action #'index/handle-index}]
   ["/re-frame" {:action #'re-frame/handle-index}]
   ["/assets/*" (ring/create-resource-handler {:path "/"})]

   ["/swagger-ui" {:no-doc true
                   :get {:action
                         (fn [state]
                           (assoc state
                                  :response
                                  (-> "swaggerui.html"
                                      (ring.util.response/resource-response {:root "public"})
                                      (ring.util.response/header "Content-Type" "text/html; charset=utf-8"))))}}]
   ^{:no-doc true}
   ["/swagger.json" {:action (fn [state]
                               (assoc state
                                      :response
                                      (ring.util.response/response
                                       (str (clojure.walk/stringify-keys (-> state :deps :swagger-data))))))}]])

(defn ->system
  [app-cfg]
  (-> (config/config app-cfg)
      (rename-key :framework.app/auth :auth)
      routes/reset
      xsw/->swagger-data
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
