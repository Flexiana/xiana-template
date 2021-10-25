(ns {{sanitized-name}}
  (:require
    [controllers.index :as index]
    [controllers.re-frame :as re-frame]
    [framework.interceptor.core :as xiana-interceptors]
;    [framework.session.backend :as session-backend]
    [framework.webserver.core :as ws]
    [framework.config.core :as config]
    [framework.coercion.core :as coercion]
;    [framework.db.storage :as db.storage]
    [framework.route.core :as routes]))

(def routes
  [["/" {:action index/index}]
   ["/re-frame" {:action re-frame/index}]
   ;; ["/assets/*" (ring/create-resource-handler {:path "/"})]
   ])

(defn system
  [config]
  (let [deps
        {
         ;; :db         (db.storage/->postgresql config)
         :routes (routes/reset routes)
         :webserver (:framework.app/web-server config)
         :router-interceptors     []
         :controller-interceptors [(xiana-interceptors/muuntaja)
                                   xiana-interceptors/params
                                   coercion/interceptor]}]
    (assoc deps :web-server (ws/start deps))))

(defn -main
  [& _args]
  (let [config (config/env)]
    (system config)))
