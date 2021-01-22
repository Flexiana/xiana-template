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
    (->files data
             ["src/backend/app/controllers/index.clj" (render "src/backend/app/controllers/index.clj" data)]
             ["src/backend/app/controllers/status.clj" (render "src/backend/app/controllers/status.clj" data)]
             ["src/backend/components/components.clj" (render "src/backend/components/components.clj" data)]
             ["src/backend/components/app.clj" (render "src/backend/components/app.clj" data)]
             ["src/backend/components/web_server.clj" (render "src/backend/components/web_server.clj" data)]
             ["src/frontend/deps.cljs" (render "src/frontend/deps.cljs" data)]
             ["src/frontend/{{sanitized-name}}/config.cljs" (render "src/frontend/app_name/config.cljs" data)]
             ["src/frontend/{{sanitized-name}}/core.cljs" (render "src/frontend/app_name/core.cljs" data)]
             ["src/frontend/{{sanitized-name}}/db.cljs" (render "src/frontend/app_name/db.cljs" data)]
             ["src/frontend/{{sanitized-name}}/events.cljs" (render "src/frontend/app_name/events.cljs" data)]
             ["src/frontend/{{sanitized-name}}/subs.cljs" (render "src/frontend/app_name/subs.cljs" data)]
             ["src/frontend/{{sanitized-name}}/views.cljs" (render "src/frontend/app_name/views.cljs" data)]
             ["resources/public/index.html" (render "resources/public/index.html" data)]
             ["config/dev/config.edn" (render "config/dev/config.edn" data)]
             ["config/test/config.edn" (render "config/test/config.edn" data)]
             ["test/status_test.clj" (render "test/status_test.clj" data)]
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             [".gitignore" (render "gitignore" data)]
             [".hgignore" (render "hgignore" data)])))
