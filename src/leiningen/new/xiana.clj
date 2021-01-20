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
             ["app/controllers/status.clj" (render "app/controllers/status.clj" data)]
             ["components/components.clj" (render "components/components.clj" data)]
             ["components/app.clj" (render "components/app.clj" data)]
             ["components/web_server.clj" (render "components/web_server.clj" data)]
             ["config/dev/config.edn" (render "config/dev/config.edn" data)]
             ["config/test/config.edn" (render "config/test/config.edn" data)]
             ["test/status_test.clj" (render "test/status_test.clj" data)]
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             [".gitignore" (render "gitignore" data)]
             [".hgignore" (render "hgignore" data)])))
