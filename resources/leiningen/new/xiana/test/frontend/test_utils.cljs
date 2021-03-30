(ns frontend.test-utils
  (:require
   ["@sinonjs/fake-timers" :as timer]
   ["@testing-library/react" :as rtl]
   ["@testing-library/user-event" :as rtue]
   [goog.dom :as gdom]
   [reagent.core :as r]
   [re-frame.core :as rf]))

(def test-container-id "tests-container")
(defn create-tests-container!
  []
  (let [container (gdom/createDom "div" #js {:id test-container-id})]
    (gdom/appendChild (-> js/document .-body) container)
    container))

(defn with-mounted-component
  [comp f]
  (let [container         (create-tests-container!)
        mounted-component (rtl/render (r/as-element comp)
                                      #js {"container" container})]
    (try
      (f mounted-component)
      (finally
        (rtl/cleanup)))))

(defn rf-with-mounted-component
  [initial-db comp f]
  (let [container         (create-tests-container!)
        mounted-component (rtl/render (r/as-element comp)
                                      #js {"container" container})]
    (rf/reg-event-db
        ::test-db
      (fn [_ _]
        initial-db))
    (rf/dispatch-sync [::test-db])
    (try
      (f mounted-component)
      (finally
        (rtl/cleanup)))))

(defn ->action-map
  [v]
  (clj->js (if (map? v)
             v
             {:target {:value v}})))

(defn click-element!
  ([el]
   (click-element! el {}))
  ([el v-or-m]
   (let [click-fn! (.. rtue -default -click)]
     (click-fn! el (->action-map v-or-m)))
   (r/flush)))

(defn double-click-element!
  ([el]
   (let [click-fn! (.. rtue -default -dblClick)]
     (click-fn! el {}))
   (r/flush)))

(defn submit!
  [el]
  (.submit rtl/fireEvent el)
  (r/flush))

(defn click-context-menu!
  [el]
  (.contextMenu rtl/fireEvent el)
  (r/flush))

(defn input-element!
  [el v-or-m]
  (.input rtl/fireEvent el (->action-map v-or-m))
  (r/flush))

(defn change-element!
  [el v-or-m]
  (.change rtl/fireEvent el (->action-map v-or-m))
  (r/flush))

(defn install-timer
  []
  (.install timer (.-getTime js/Date.)))

(defn tick
  [t x]
  (.tick t x))

(defn uninstall-timer
  [t]
  (.uninstall t))
