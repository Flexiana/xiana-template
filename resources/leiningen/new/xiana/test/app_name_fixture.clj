(ns {{sanitized-name}}-fixture
  (:require
    [migratus.core :as migratus]
    [next.jdbc :as jdbc])
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
                        :subname (str "//localhost:" pg-port "/{{sanitized-name}}")))]
    (jdbc/execute! (dissoc db-config :dbname) [init-sql])
    (assoc config :framework.db.storage/postgresql db-config)))

(defn migrate!
  [config]
  (let [db (:framework.db.storage/postgresql config)
        mig-config (assoc (:framework.db.storage/migration config) :db db)]
    (migratus/migrate mig-config))
  config)

(defn std-system-fixture
  [f]
  (let [config (config/edn)
        system (-> config
                   embedded-postgres!
                   migrate!
                   {{sanitized-name}}/system
                   component/start)]
    (try
      (f)
      (finally
        (component/stop system)))))

