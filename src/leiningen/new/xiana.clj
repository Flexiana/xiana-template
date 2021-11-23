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
    (apply ->files data (->> ["dev/user.clj"
                              "src/frontend/deps.cljs"
                              "src/shared/config.clj"
                              "src/shared/schema.clj"
                              "resources/public/index.html"
                              "config/dev/config.edn"
                              "config/test/config.edn"
                              "project.clj"
                              "docker-compose.yml"
                              "README.md"]
                             (map (fn [path] [path (render path data)]))
                             (concat
                               [["src/backend/{{name-to-path}}/core.clj" (render "src/backend/app_name/core.clj" data)]
                                ["src/backend/{{name-to-path}}/controllers/index.clj" (render "src/backend/app_name/controllers/index.clj" data)]
                                ["src/backend/{{name-to-path}}/controllers/re_frame.clj" (render "src/backend/app_name/controllers/re_frame.clj" data)]
                                ["src/backend/{{name-to-path}}/controller_behaviors/.gitkeep" (render "src/backend/app_name/controller_behaviors/.gitkeep" data)]
                                ["src/backend/{{name-to-path}}/interceptors/.gitkeep" (render "src/backend/app_name/interceptors/.gitkeep" data)]
                                ["src/backend/{{name-to-path}}/models/.gitkeep" (render "src/backend/app_name/models/.gitkeep" data)]
                                ["src/backend/{{name-to-path}}/views/layouts/.gitkeep" (render "src/backend/app_name/views/layouts/.gitkeep" data)]
                                ["src/backend/{{name-to-path}}/interceptors.clj" (render "src/backend/app_name/interceptors.clj" data)]
                                ["src/backend/{{name-to-path}}/views/common.clj" (render "src/backend/app_name/views/common.clj" data)]
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
