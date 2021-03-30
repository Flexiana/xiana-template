(ns leiningen.new.xiana
  (:require
    [clojure.set :as set]
    [clojure.string :as st]
    [clojure.string :as str]
    [leiningen.core.main :as main]
    [leiningen.new.base :as base]
    [leiningen.new.helpers :as helpers]
    [leiningen.new.nubank :as nubank]
    [leiningen.new.templates :refer [multi-segment sanitize-ns name-to-path ->files]]))


(declare template-data check-options app-files)


(def available-options
  #{nubank/option})


(defn xiana
  [name & options]
  (println options)
  (let [data (template-data name options)]
    (check-options options)
    (main/info "Generation fresh 'lein new' xiana project.")
    (apply ->files data (app-files data options))))


(defn check-available
  [options]
  (let [options-set (into #{} options)
        abort? (not (set/superset? available-options options-set))]
    (when abort?
      (main/abort "\nError: invalid option(s)\nAvailable: "
                  (st/join " " (sort available-options)) "\n"))))


(defn check-options
  [options]
  (doto options
    check-available))


(defn template-data
  [name options]
  {:name name
   :namespace (multi-segment (sanitize-ns name))
   :sanitized-name (sanitize-ns name)
   :name-to-path (name-to-path name)
   :nubank? (helpers/options? "+nubank" options)})


(defn app-files
  [data options]
  (concat
    (when (helpers/options? nubank/option options) (nubank/files data))
    (base/files data options)))
