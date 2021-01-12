(ns {{sanitized-name}}.controllers.task
  (:require [corpus.malli :as malli]
            [corpus.model :as model]
            [corpus.responses :as responses]
            [corpus.api :as corpus]
            [corpus.widgets :as widgets]
            [clojure.java.jdbc :as jdbc])
  (:import (java.time LocalDateTime)
           (java.time.format DateTimeFormatter)))

(def now
  (LocalDateTime/of 2020 11 29 13 0 0))

(defn objs [db]
  (->> "SELECT * FROM tasks"
       (jdbc/query db)
       (map #(assoc % :created-at now))
       (into [])))

(defmethod model/formatter ::created-at-humanized
  [req obj field-id formatter-id]
  (.format (DateTimeFormatter/ofPattern "yyyy.MM.dd HH:mm")
           (:created-at obj)))

(defmethod model/formatter ::full-name
  [req obj field-id formatter-id]
  (str (:first-name obj) " " (:last-name obj)))

(def User
  [:map
   [:id [:string
         {:title "ID"
          ::model/pk? true
          ::model/readonly? true}]]
   [:first-name [:string
                 {::model/widget ::widgets/text
                  :title "First name"}]]
   [:last-name [:string
                {::model/widget ::widgets/text
                 :title "Last name"}]]
   [:full-name [:string
                {:title "Full name"
                 ::model/formatter ::full-name}]]
   [:created-at [:string
                 {:title "Created at"
                  ::model/readonly? true}]]
   [:created-at-humanized [:string
                           {:title "Created at"
                            ::model/formatter ::created-at-humanized}]]
   [:age [pos-int?
          {::model/widget ::widgets/text
           :title "Age"}]]])

(def user-transformer (malli/transform-model User))

(defn render-index
  [{:keys [spec] :as _db} req]
  ;; TODO: pass data for pagination
  (responses/as-index (objs spec)))

(defn render-edit
  [{:keys [spec] :as _db} req]
  (let [id (-> req :path-params :id)]
    (responses/as-edit (some #(when (= id (str (:id %))) %)
                             (objs spec)))))

(def controller
  (corpus/controller->
   (corpus/model (malli/schema->model User))
   (corpus/path "/tasks")
   (corpus/index-fields [:id :first-name :age :full-name :created-at-humanized])
   (corpus/render-index render-index)
   (corpus/edit-model (malli/schema->model User))
   (corpus/edit-fields [:id :first-name :last-name :created-at :age])
   (corpus/render-edit render-edit)
   (corpus/edit-invalid-form-errors ["Please correct the errors below"])
   (corpus/validate-edit-form (partial malli/validator User user-transformer))
   (corpus/edit-valid-flash-messages [{:text "The user has been updated"
                                       :severity :success}])))

;; (def controller
;;   {:model (malli/schema->model User)
;;    :path "/tasks"
;;    :get-index-fields
;;    (constantly [:id :first-name :age :full-name :created-at-humanized])
;;    :render-index render-index
;;    :get-edit-model
;;    (fn get-edit-model [req obj]
;;      (malli/schema->model User))
;;    :get-edit-fields
;;    (constantly [:id :first-name :last-name :age :created-at])
;;    :render-edit
;;    (fn render-edit [req]
;;      (let [id (-> req :path-params :id)]
;;        (responses/as-edit (some #(when (= id (str (:id %))) %)
;;                                 objs))))
;;    :handle-edit-valid
;;    (fn handle-edit-valid [req {:keys [value]}]
;;      ;; a response could be overridden here
;;      {})
;;    :get-edit-invalid-form-errors
;;    (fn get-edit-invalid-form-errors [req {:keys [value humanized-errors]}]
;;      ["Please correct the errors below"])
;;    :handle-edit-invalid
;;    (fn handle-edit-invalid [req {:keys [value humanized-errors]}]
;;      ;; a response could be overridden here
;;      {})
;;    :get-edit-valid-flash-messages
;;    (fn get-edit-valid-flash-messages [req {:keys [value]}]
;;      [{:text "The user has been updated"
;;        :severity :success}])
;;    :validate-edit-form
;;    (partial malli/validator User user-transformer)})
