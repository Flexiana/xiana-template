(defproject {{name}} "0.1.0-SNAPSHOT"
            :description "FIXME: write description"
            :min-lein-version "2.0.0"
            :dependencies [[org.clojure/clojure "1.10.1"]
                           [duct/core "0.8.0"]
                           [duct/module.logging "0.5.0"]
                           [duct/server.http.jetty "0.2.1"]
                           [funcool/cats "2.4.1"]
                           [integrant "0.8.0"]
                           [hiccup "2.0.0-alpha2"]
                           [metosin/reitit "0.5.6"]
                           [ring/ring-defaults "0.3.2"]
                           [garm "0.8.0"]
                           [metosin/malli "0.2.1"]
                           [kerodon "0.9.1"]
                           [duct/module.sql "0.5.0"]
                           [org.xerial/sqlite-jdbc "3.27.2"]
                           [com.flexiana/corpus "0.1.0"]]
            :plugins [[duct/lein-duct "0.12.1"]]
            :main ^:skip-aot {{name}}.main
            :uberjar-name "{{name}}.jar"
            :resource-paths ["resources" "target/resources"]
            :prep-tasks ["javac" "compile" ["run" ":duct/compiler"]]
            :middleware [lein-duct.plugin/middleware]
            :cloverage {:fail-threshold   98
                        :ns-exclude-regex [#"{{name}}.main"
                                           #"dev"]}
            :eastwood {:config-files       ["eastwood.clj"]
                       :exclude-linters    [:no-ns-form-found]
                       :exclude-namespaces [dev]
                       :namespaces         [:source-paths :test-paths]}
            :profiles
            {:dev          [:project/dev :profiles/dev]
             :repl         {:prep-tasks   ^:replace ["javac" "compile"]
                            :repl-options {:init-ns user}}
             :uberjar      {:aot :all}
             :profiles/dev {}
             :project/dev  {:source-paths   ["dev/src"]
                            :resource-paths ["dev/resources"]
                            :dependencies   [[integrant/repl "0.3.2"]
                                             [clj-kondo "2020.09.09"]
                                             [eftest "0.5.9"]
                                             [hawk "0.2.11"]
                                             [ring/ring-mock "0.4.0"]]
                            :plugins        [[jonase/eastwood "0.3.11"]
                                             [lein-ancient "0.6.15"]
                                             [lein-cloverage "1.2.1"]
                                             [lein-kibit "0.1.8"]]}}
            :aliases {"ci"    ["do" "clean," "cloverage," "lint," "uberjar"]
                      "kondo" ["run" "-m" "clj-kondo.main" "--lint" "src" "test"]
                      "lint"  ["do" "kondo," "eastwood," "kibit"]})
  