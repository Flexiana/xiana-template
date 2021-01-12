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
             ["project.clj" (render "project.clj" data)]
             ["src/{{name-to-path}}/main.clj" (render "app/main.clj" data)]
             ["src/{{name-to-path}}/handler.clj" (render "app/handler.clj" data)]
             ["src/{{name-to-path}}/controllers/task.clj" (render "app/controllers/task.clj" data)]
             ["src/{{name-to-path}}/routes/status.clj" (render "app/routes/status.clj" data)]
             ["test/{{name-to-path}}/integration_test.clj" (render "test/integration_test.clj" data)]
             ["src/duct_hierarchy.edn" (render "duct_hierarchy.edn" data)]
             ["resources/{{name-to-path}}/config.edn" (render "config.edn" data)]
             ["dev/resources/dev.edn" (render "dev/resources/dev.edn" data)]
             ["dev/resources/local.edn" (render "dev/resources/local.edn" data)]
             ["dev/resources/test.edn" (render "dev/resources/test.edn" data)]
             ["dev/src/dev.clj" (render "dev/src/dev.clj" data)]
             ["dev/src/local.clj" (render "dev/src/local.clj" data)]
             ["dev/src/user.clj" (render "dev/src/user.clj" data)]
             ["README.md" (render "README.md" data)]
             ["db/.gitignore" (render "db.gitignore" data)]
             [".gitignore" (render "gitignore" data)])))
