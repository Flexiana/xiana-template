(ns leiningen.new.nubank
  (:require
    [leiningen.new.helpers :as helpers]))


(def option "+nubank")


(defn files
  [data]
  [["src/frontend/{{name-to-path}}/workspaces.cljs" (helpers/render "src/frontend/app_name/workspaces.cljs" data)]
   ["resources/public/workspaces.html" (helpers/render "resources/public/workspaces.html" data)]])
