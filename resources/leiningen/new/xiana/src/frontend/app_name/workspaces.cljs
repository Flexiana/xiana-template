(ns {{sanitized-name}}.workspaces
  (:require
    [{{sanitized-name}}.events :as events]
    [{{sanitized-name}}.views :as views]
    [nubank.workspaces.card-types.react :refer [react-card]]
    [nubank.workspaces.core :as ws]
    [nubank.workspaces.model :as wsm]
    [re-frame.core :as re-frame]
    [reagent.core :as r]))

(defonce init (ws/mount))

(re-frame/clear-subscription-cache!)

(ws/defcard init-card
            (re-frame/dispatch-sync [::events/initialize-db])
            (react-card
              (r/as-element [views/main-panel])))
