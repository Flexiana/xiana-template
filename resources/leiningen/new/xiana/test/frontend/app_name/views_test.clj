(ns frontend.{{sanitized-name}}.views-test
    (:require [cljs.test :as t
               :include-macros true
               :refer [are is testing deftest]]
              [re-frame.core :as re-frame]
              [{{sanitized-name}}.db :as db]
              [{{sanitized-name}}.views :as views]
              [matcher-combinators.test :refer [match?]]
              [frankie.views :as views]
              [frankie.db :as db]
              [reagent.core :as r]
              [frontend.test-utils :as u]
              [re-frame.core :as rf]
              [day8.re-frame.test :as rf-test]))

(rf/clear-subscription-cache!)

(deftest example-counter-test
  (rf-test/run-test-sync
   (let [rendered-div # (.getByTestId % "hello")]
     (u/rf-with-mounted-component
      db/default-db
      [views/main-panel]
      (fn [comp]
        (is (= "Hello from re-frame" (rendered-div comp))))))))
