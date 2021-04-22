(ns {{sanitized-name}}-fixture
  (:require
    [next.jdbc :as jdbc]
    [framework.config.core :as config]
    [com.stuartsierra.component :as component]
    [{{sanitized-name}} :refer [system sys-deps app-config routes]])
  (:import
    (com.opentable.db.postgres.embedded
      EmbeddedPostgres)))


(defn embedded-postgres!
  [config]
  (let [pg (.start (EmbeddedPostgres/builder))
        pg-port (.getPort pg)
        init-sql (slurp "./Docker/init.sql")
        db-config (-> config
                      :framework.db.storage/postgresql
                      (assoc
                        :port pg-port
                        :embedded pg
                        :subname (str "//localhost:" pg-port "/{{sanitized-name}}")))]
    (jdbc/execute! (dissoc db-config :dbname) [init-sql])
    (assoc config :framework.db.storage/postgresql db-config)))

(defn std-system-fixture
  [f]
  (let [system (-> (config/edn)
                   (assoc-in [:framework.app/web-server :port] 3333)
                   embedded-postgres!
                   (system app-config routes)
                   component/map->SystemMap
                   (component/system-using sys-deps)
                   component/start)]
    (try
      (f)
      (finally
        (component/stop system)))))
