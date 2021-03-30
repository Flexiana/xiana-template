(ns {{sanitized-name}}.views
  (:require
   [re-frame.core :as re-frame]
   [{{sanitized-name}}.subs :as subs]
   ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 {:data-testid "hello"}
      "Hello from " @name]
     ]))
