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
            [schema.core :as s]
            [my-api.utilities :as utilities]))


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


(s/defschema FerryCompany
  {:name              s/Str
   :number-of-ferries Long
   :country           (s/enum :France :Netherlands)
   (s/optional-key
     :description)    s/Str})

(s/defschema DiceRollResult
  {:result s/Int})

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
      :data {:info {:title       "Pizza and Ferries"
                    :description "A simple API using the Compojure Api library"}
             :tags [{:name        "Pizza and Ferries api",
                     :description "All your favourite Pizza, perhaps no ferries though"}]}}}

    (context "/api" []
             :tags ["Pizza and Ferries api"]          ; Use the tag in the swagger UI

             ;; /plus takes two values and returns a JSON result
             (GET "/plus" []
                  :return {:result Long}
                  :query-params [x :- Long, y :- Long]
                  :summary "adds two numbers together"
                  (ok {:result (+ x y)}))

             ;; Added a two number calculator, lisp style, returns JSON result
             (GET "/calculator" []
                  :return {:result Long}
                  :query-params [op :- String, x :- Long, y :- Long]
                  :summary "Calculates with two numbers"
                  (ok {:result (utilities/calculator op x y)}))

             ;; http://localhost:3000/api/dice-roll
             (GET "/dice-roll" []
                  :return DiceRollResult
                  :summary "Rolls a twenty-sided die for Dungeon's and Dragons"
                  (ok {:result (inc (rand-int 20))}))

             (POST "/pizza" []
                   :return Pizza
                   :body [pizza Pizza]
                   :summary "returns a Pizza, if the Pizza is one that can be made"
                   (ok pizza))

             (POST "/ferry-company" [number-of-ferries]
                   :return FerryCompany
                   :body [ferry-company FerryCompany]
                   :summary "commissions a ferry company, if it has ferries"
                   (ok ferry-company)))))


;; A -main or -dev-main function is not provided as we are using
;; the lein-ring plugin, which injects this for us so it can
;; manage the reloading of our code during development.



;; Test JSON data
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; for the Pizza echo api

;; {
;;  "name"        : "Spicy Vegitarian Pepperoni",
;;  "size"        : "XL",
;;  "origin"      : {
;;                   "country" : "PO",
;;                   "city"    : "Tampere"
;;                   },
;;  "description" : "Healthy and delicious Vegitain version of a double pepperoni pizza with some jalapenos to spice it up"
;;  }

;; Oh no, they do not do pizza in XL size...

;; Response Body
;; {
;;  "errors" : {
;;              "size" : "(not (#{:L :M :S} :XL))"
;;              }
;;  }


;; same data but using curl on the command line

;; curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \
;;                        "name" : "Spicy Vegitarian Pepperoni", \
;;                        "size" : "XL", \
;;                        "origin" : { \
;;                                    "country" : "PO",     \
;;                                    "city"    : "Tampere" \
;;                                   }, \
;;                        "description" : "Healthy and delicious Vegitain version of a double pepperoni pizza with some jalapenos to spice it up" \
;;                       }' 'http://localhost:3000/api/echo'
