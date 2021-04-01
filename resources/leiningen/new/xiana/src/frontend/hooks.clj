(ns hooks
  (:require
    [clojure.java.io :as io]
    [clojure.java.shell :refer [sh]]
    [clojure.string :as s]))


(defn exec
  [& cmd]
  (let [cmd (s/split (s/join " " (flatten cmd)) #"\s+")
        _ (println (s/join " " cmd))
        {:keys [exit out err]} (apply sh cmd)]
    (if (zero? exit)
      (when-not (s/blank? out)
        (println out))
      (println err))))


(defn npm-init!
  {:shadow.build/stage :configure}
  [build-state]
  (if-not (and (.isDirectory (io/file "node_modules"))
               (not (= 0 (count (.list (io/file "node_modules"))))))
    (do (println ";;=>> 'Installing node_modules...")
        (exec "npm install")
        build-state)
    (do (println ";;=>> 'npm is already initialized in the current project'")
        build-state)))


(defn purge-css!
  {:shadow.build/stage :flush}
  [build-state]
  (case (:shadow.build/mode build-state)
    :release
    (do
      (println ";;=> 'Running prebuild script with -env production'")
      (exec "npm run-script prebuild")
      build-state)
    :dev
    (do
      (println ";;=> 'Runnnig preserve script'")
      (exec "npm run-script preserve")
      build-state)))
