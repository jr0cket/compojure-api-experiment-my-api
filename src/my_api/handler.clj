;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; my-api.handler
;;
;; A project to explore the use of clojure-api library
;; to create relatively simple API
;;
;; Date started: 22nd February 2019
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns my-api.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))


;; Data definition
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Defining a Pizza using prismatic/schema (could also use clojure.spec)
(s/defschema Pizza
  {:name           s/Str
   :size           (s/enum :L :M :S)
   :origin         {:country (s/enum :FI :PO)
                    :city    s/Str}
   (s/optional-key
     :description) s/Str})

;; Application
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; The application that is loaded into our web container.
;; A server will run and listen to the end points defined
;; by the request type (GET, POST, etc.)
;; and the web address (URI)
;; The API contains a swagger definition for our API's
(def app
  (api
    {:swagger
     {:ui   "/"
      :spec "/swagger.json"
      :data {:info {:title       "My-api"
                    :description "Compojure Api example"}
             :tags [{:name "api", :description "some apis"}]}}}

    (context "/api" []
             :tags ["api"]

             (GET "/plus" []
                  :return {:result Long}
                  :query-params [x :- Long, y :- Long]
                  :summary "adds two numbers together"
                  (ok {:result (+ x y)}))

             (POST "/echo" []
                   :return Pizza
                   :body [pizza Pizza]
                   :summary "echoes a Pizza"
                   (ok pizza)))))


;; A -main or -dev-main function is not provided as we are using
;; the lein-ring plugin, which injects this for us so it can
;; manage the reloading of our code during development.

