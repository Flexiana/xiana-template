(ns leiningen.new.base
  (:require [leiningen.new.helpers :as helpers]))

(def file-paths
  ["Docker/db.Dockerfile"
   "Docker/init.sql"
   "src/backend/app/controllers/index.clj"
   "src/backend/app/controllers/re_frame.clj"
   "src/backend/app/controller_behaviors/.gitkeep"
   "src/backend/app/db_migrations/.gitkeep"
   "src/backend/app/interceptors/.gitkeep"
   "src/backend/app/models/.gitkeep"
   "src/backend/app/views/layouts/.gitkeep"
   "src/backend/app/interceptors.clj"
   "src/frontend/deps.cljs"
   "src/frontend/base.css"
   "src/frontend/hooks.clj"
   "src/shared/config.clj"
   "src/shared/schema.clj"
   "resources/public/index.html"
   "config/dev/config.edn"
   "config/test/config.edn"
   "project.clj"
   "package.json"
   "postcss.config.js"
   "tailwind.config.js"
   "docker-compose.yml"
   "postgres-start.sh"
   "README.md"])

(defn files
  [data options]
  (->> file-paths
       (map (fn [path] [path (helpers/render path data)]))
       (concat
        [["src/backend/{{name-to-path}}.clj" (helpers/render "src/backend/app_name.clj" data)]
         ["src/frontend/{{name-to-path}}/config.cljs" (helpers/render "src/frontend/app_name/config.cljs" data)]
         ["src/frontend/{{name-to-path}}/core.cljs" (helpers/render "src/frontend/app_name/core.cljs" data)]
         ["src/frontend/{{name-to-path}}/db.cljs" (helpers/render "src/frontend/app_name/db.cljs" data)]
         ["src/frontend/{{name-to-path}}/events.cljs" (helpers/render "src/frontend/app_name/events.cljs" data)]
         ["src/frontend/{{name-to-path}}/subs.cljs" (helpers/render "src/frontend/app_name/subs.cljs" data)]
         ["src/frontend/{{name-to-path}}/views.cljs" (helpers/render "src/frontend/app_name/views.cljs" data)]
         ["test/{{name-to-path}}_test.clj" (helpers/render "test/app_name_test.clj" data)]
         [".gitignore" (helpers/render "gitignore" data)]
         ["shadow-cljs.edn" (helpers/render "shadow-cljs.edn" data)]
         [".hgignore" (helpers/render "hgignore" data)]])))
