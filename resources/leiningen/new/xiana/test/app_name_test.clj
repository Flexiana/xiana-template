(ns {{sanitized-name}}-test
  (:require
    [clj-http.client :as http]
    [clojure.test :refer [deftest is use-fixtures]]
    [com.stuartsierra.component :as component]
    [{{sanitized-name}}]
    [{{sanitized-name}}-fixture :refer [std-system-fixture]]
    [framework.config.core :as config]))


(use-fixtures :once std-system-fixture)

(deftest index-test
  (is (= {:body   "Index page"
          :status 200}
         (-> {:url                  "http://localhost:3000/"
              :unexceptional-status (constantly true)
              :method               :get}
             http/request
             (select-keys [:status :body])))))
