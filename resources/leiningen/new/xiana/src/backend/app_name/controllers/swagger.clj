(ns {{sanitized-name}}.controllers.swagger
  (:require
   [ring.util.response]))

(defn create-swagger-ui-route-map []
  {:get {:action
         (fn [state]
           (assoc state
                  :response
                  (-> "swaggerui.html"
                      (ring.util.response/resource-response {:root "public"})
                      (ring.util.response/header "Content-Type" "text/html; charset=utf-8"))))}})

(defn create-swagger-data-route-map []
  {:action (fn [state]
             (assoc state
                    :response
                    (ring.util.response/response
                     (str (-> state :deps ((-> state :deps :xiana/swagger :path)))))))})
