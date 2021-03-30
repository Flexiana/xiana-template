(defproject {{sanitized-name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.flexiana/framework "0.1.4"]
                 [thheller/shadow-cljs "2.11.26"]
                 [clj-http "3.12.0"]
                 [reagent "0.10.0"]
                 [re-frame "1.1.2"]]
  :plugins [[migratus-lein "0.7.3"]
            [lein-shell "0.5.0"]]
  :main ^:skip-aot {{name}}
  :uberjar-name "{{name}}.jar"
  :source-paths ["src/backend" "src/backend/app" "src/frontend" "src/shared"]
  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]
  :profiles {:dev   {:resource-paths ["config/dev"]
                     :dependencies [[binaryage/devtools "1.0.2"]]}
             :local {:resource-paths ["config/local"]}
             :prod  {:resource-paths ["config/prod"]}
             :test  {:resource-paths ["config/test"]
                     :dependencies   [{{#workspaces?}}[nubank/workspaces "1.0.16"]{{/workspaces?}}
                                      [binaryage/devtools "1.0.2"]
                                      [nubank/matcher-combinators "3.1.4"]
                                      [day8.re-frame/test  "0.1.5"]
                                      [kerodon "0.9.1"]]}}
  :aliases {"ci"         ["do" "clean," "cloverage," "lint," "uberjar"]
            "kondo"      ["run" "-m" "clj-kondo.main" "--lint" "src" "test"]
            "repl"       ["with-profile" "+test" "run" "-m" "shadow.cljs.devtools.cli" "server"]
            "workspaces" ["with-profile" "+test" "run" "-m" "shadow.cljs.devtools.cli" "watch" "workspaces"]
            "app"        ["with-profile" "+test" "run" "-m" "shadow.cljs.devtools.cli" "watch" "app"]
            "lint"       ["do" "kondo," "eastwood," "kibit"]
            "release"    ["with-profile" "prod" "run" "-m" "shadow.cljs.devtools.cli" "release" "app"]}
  :migratus {:store         :database
             :migration-dir "migrations"
             :db            {:classname   "com.mysql.jdbc.Driver"
                             :subprotocol "postgres"
                             :subname     "//localhost:5433/frankie"
                             :user        "postgres"
                             :password    "postgres"}})
