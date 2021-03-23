(ns {{sanitized-name}}-test
  (:require
    [clj-http.client :as http]
    [clojure.test :refer [deftest is use-fixtures]]
    [{{sanitized-name}}]
    [{{sanitized-name}}-fixture :refer [std-system-fixture]]))


(use-fixtures :once std-system-fixture)

(deftest index-test
  (is (= {:body   "Index page"
          :status 200}
         (-> {:url                  "http://localhost:3000/"
              :unexceptional-status (constantly true)
              :method               :get}
             http/request
             (select-keys [:status :body])))))
