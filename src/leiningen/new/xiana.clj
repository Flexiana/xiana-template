(ns leiningen.new.xiana
  (:require [leiningen.new.templates :refer [multi-segment sanitize-ns renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "xiana"))

(defn xiana
  "FIXME: write documentation"
  [name]
  (let [data {:name name
              :namespace (multi-segment (sanitize-ns name))
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' xiana project.")
    (->files data
             ["project.clj" (render "project.clj" data)]
             ["test/{{sanitized}}/core_test.clj" (render "test.clj" data)]
             ["src/{{sanitized}}/core.clj" (render "core.clj" data)]
             ["src/duct_hierarchy.edn" (render "duct_hierarchy.edn" data)]
             ["resources/{{sanitized}}/config.edn" (render "config.edn" data)]
             ["README.md" (render "README.md" data)]
             [".gitignore" (render "gitignore" data)])))
