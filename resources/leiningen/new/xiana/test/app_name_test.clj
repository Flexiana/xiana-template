(ns {{sanitized-name}}-test
  (:require
    [clj-http.client :as http]
    [clojure.test :refer [deftest is use-fixtures]]
    [com.stuartsierra.component :as component]
    [{{sanitized-name}}]
    [framework.config.core :as config]))

(defn std-system-fixture
  [f]
  (let [config (config/edn)
        system (-> config
                   {{sanitized-name}}/system
                   component/start)]
    (try
      (f)
      (finally
        (component/stop system)))))

(use-fixtures :each std-system-fixture)

(deftest index-test
  (is (= {:body   "Index page"
          :status 200}
         (-> {:url                  "http://localhost:3000/"
              :unexceptional-status (constantly true)
              :method               :get}
             http/request
             (select-keys [:status :body])))))
