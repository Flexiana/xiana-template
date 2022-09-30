(defproject {{sanitized-name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :min-lein-version "2.0.0"
  :dependencies [[com.flexiana/framework "0.5.0-rc3"]
                 [piotr-yuxuan/closeable-map "0.36.2"]
                 [org.clojure/tools.namespace "1.3.0"]
                 [thheller/shadow-cljs "2.20.2"]
                 [com.fzakaria/slf4j-timbre "0.3.21"]
                 [reagent "0.10.0"]
                 [re-frame "1.1.2"]]
  :plugins [[lein-shadow "0.4.0"]]
  :main ^:skip-aot {{name}}.core
  :uberjar-name "{{name}}.jar"
  :source-paths ["src/backend" "src/frontend" "src/shared"]
  :clean-targets ^{:protect false} ["resources/public/assets/js/compiled" "target"]
  :profiles {:dev   {:resource-paths ["dev" "config/dev"]
                     :dependencies   [[binaryage/devtools "1.0.3"]]}
             :local {:resource-paths ["config/local"]}
             :prod  {:resource-paths ["config/prod"]}
             :test  {:resource-paths ["config/test"]
                     :dependencies   [[clj-http "3.12.3"]
                                      [clj-test-containers "0.7.2"]
                                      [kerodon "0.9.1"]]}}
  :shadow-cljs {:nrepl  {:port 8777}
                :builds {:app {:target     :browser
                               :output-dir "resources/public/assets/js/compiled"
                               :asset-path "assets/js/compiled"
                               :modules    {:app {:init-fn {{sanitized-name}}.core/init
                                                  :preloads [devtools.preload]}}}}}
  :aliases {"migrate" ["run" "-m" "xiana.db.migrate"]
            "watch"   ["with-profile" "dev" "do"
                       ["shadow" "watch" "app" "browser-test" "karma-test"]]
            "release" ["with-profile" "prod" "do"
                       ["shadow" "release" "app"]]})
