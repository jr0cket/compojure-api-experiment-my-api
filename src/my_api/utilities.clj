(ns my-api.utilities)

;; Lisp Calculator
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def operands {"+" + "-" - "*" * ":" /})

(defn calculator
  "A very simple calculator that can add, divide, subtract and multiply.  This is done through the magic of variable path elements."
  [operand number1 number2]
  (let [function (get operands operand)]
    (if function
      (function number1 number2)
      "Sorry, unknown operator.  I only recognise + - * : (: is for division)")))
