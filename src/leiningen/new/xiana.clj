(ns leiningen.new.xiana
  (:require [leiningen.new.templates :refer [multi-segment sanitize-ns renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "xiana"))

(defn xiana
  "FIXME: write documentation"
  [name]
  (let [data {:name           name
              :namespace      (multi-segment (sanitize-ns name))
              :sanitized-name (sanitize-ns name)
              :name-to-path   (name-to-path name)}]
    (main/info "Generating fresh 'lein new' xiana project.")
    (apply ->files data (->> ["Docker/db.Dockerfile"
                              "Docker/init.sql"
                              "src/backend/app/controllers/index.clj"
                              "src/backend/app/controllers/re_frame.clj"
                              "src/backend/app/controller_behaviors/.gitkeep"
                              "src/backend/app/db_migrations/.gitkeep"
                              "src/backend/app/interceptors/.gitkeep"
                              "src/backend/app/models/.gitkeep"
                              "src/backend/app/views/layouts/.gitkeep"
                              "src/backend/app/interceptors.clj"
                              "src/backend/app/views/common.clj"
                              "src/frontend/deps.cljs"
                              "src/shared/config.clj"
                              "src/shared/schema.clj"
                              "resources/public/index.html"
                              "config/dev/config.edn"
                              "config/test/config.edn"
                              "project.clj"
                              "docker-compose.yml"
                              "postgres-start.sh"
                              "README.md"]
                             (map (fn [path] [path (render path data)]))
                             (concat
                               [["src/backend/{{name-to-path}}.clj" (render "src/backend/app_name.clj" data)]
                                ["src/frontend/{{name-to-path}}/config.cljs" (render "src/frontend/app_name/config.cljs" data)]
                                ["src/frontend/{{name-to-path}}/core.cljs" (render "src/frontend/app_name/core.cljs" data)]
                                ["src/frontend/{{name-to-path}}/db.cljs" (render "src/frontend/app_name/db.cljs" data)]
                                ["src/frontend/{{name-to-path}}/events.cljs" (render "src/frontend/app_name/events.cljs" data)]
                                ["src/frontend/{{name-to-path}}/subs.cljs" (render "src/frontend/app_name/subs.cljs" data)]
                                ["src/frontend/{{name-to-path}}/views.cljs" (render "src/frontend/app_name/views.cljs" data)]
                                ["test/{{name-to-path}}_test.clj" (render "test/app_name_test.clj" data)]
                                ["test/{{name-to-path}}_fixture.clj" (render "test/app_name_fixture.clj" data)]
                                [".gitignore" (render "gitignore" data)]
                                [".hgignore" (render "hgignore" data)]])))))
