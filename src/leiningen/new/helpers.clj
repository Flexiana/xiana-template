(ns leiningen.new.helpers
  (:require [leiningen.new.templates :refer [renderer sanitize name-to-path]]
            [clojure.java.io :as io]))

(def template-name "xiana")

(def render-text (renderer template-name))

(defn resource-input
  [resource-path]
  (-> (str "leiningen/new/" (sanitize template-name) "/" resource-path)
      io/resource
      io/input-stream))

(defn render
  ([resource-path]
   (resource-input resource-path))
  ([resource-path data]
   (render-text resource-path data)))

(defn options? [option-name options]
  (boolean
   (some #{option-name} options)))
