(ns {{sanitized-name}}.integration-test
  (:require [integrant.core :as ig]
            [duct.core :as duct]
            [kerodon.core :refer :all]
            [kerodon.test :refer :all]
            [clojure.test :refer :all]))

;(deftest a-test
;  (testing "FIXME, I fail."
;    (is (= 0 1))))

(defn- start-test-system
  ([keys]
   (start-test-system [:duct.profile/test] keys))
  ([profiles keys]
   (duct/load-hierarchy)
   (let [config (-> "{{sanitized-name}}/config.edn"
                    duct/resource
                    duct/read-config
                    (duct/prep-config profiles))]
     (case keys
       :all (ig/init config)
       (ig/init config keys)))))

(deftest status-test
  (let [system (start-test-system [:{{sanitized-name}}.handler/ring])
        handler (:{{sanitized-name}}.handler/ring system)]
    (try
      (-> (session handler)
          (visit "/status")
          (has (status? 200))
          (has (some-regex? "^.*OK.*$")))
      (finally
        (ig/halt! system)))))

(deftest tasks-update-test
  (let [system (start-test-system [:duct.migrator/ragtime :{{sanitized-name}}.handler/ring])
        handler (:{{sanitized-name}}.handler/ring system)]
    (try
      (-> (session handler)
          (visit "/tasks")
          (has (some-text? "Lars"))

          (follow "Lars Larssen")
          (has (value? "Last name *" "Larssen"))

          (fill-in "Last name *" "Markus")
          (press "Update")
          (follow-redirect)
          (has (some-text? "The user has been updated"))
          (follow "Back")
          (has (some-text? "Lars Markus")))
      (finally
        (ig/halt! system)))))
