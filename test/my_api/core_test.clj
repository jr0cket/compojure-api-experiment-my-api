(ns my-api.core-test
  (:require [cheshire.core :as cheshire]
            [clojure.test :refer :all]
            [my-api.handler :refer :all]
            [ring.mock.request :as mock]))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(deftest adder-test

  (testing "Test GET request to /api/plus?x={number}&y={number} returns expected response"
    (let [response (app (-> (mock/request :get  "/api/plus?x=1&y=2")))
          body     (parse-body (:body response))]
      (is (= (:status response) 200))
      (is (= (:result body) 3)))))


;; reading in JSON files
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; slurp will read files into our Clojure code.

#_(slurp "spicy-vegan-pepperoni.json")
;; => "{\n    \"name\"        : \"Spicy Vegan Pepperoni\",\n    \"size\"        : \"XL\",\n    \"origin\"      : {\n        \"country\" : \"FI\",\n        \"city\"    : \"Tampere\"\n    },\n    \"description\" : \"Healthy and delicious Vegan version of a double pepperoni pizza with some jalapenos to spice it up\"\n}\n"

;; We can use cheshire library to convert the JSON to a Clojure data structure.

#_(cheshire/parse-string
    (slurp "spicy-vegan-pepperoni.json"))
;; => {"name" "Spicy Vegan Pepperoni", "size" "XL", "origin" {"country" "PO", "city" "Tampere"}, "description" "Healthy and delicious Vegan version of a double pepperoni pizza with some jalapenos to spice it up"}


#_(clojure.pprint/pprint
    (cheshire/parse-string
      (slurp "spicy-vegan-pepperoni.json")))
;; => nil

;; From the REPL output
;; {"name"   "Spicy Vegitarian Pepperoni",
;;  "size"   "XL",
;;  "origin" {"country" "PO", "city" "Tampere"},
;;  "description"
;;  "Healthy and delicious Vegitain version of a double pepperoni pizza with some jalapenos to spice it up"}
