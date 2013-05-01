(ns wishful.core
  (:require [wishful.redefs :as redefs]
            [wishful.stubs :as stubs]))

(defn make-stub
  "Returns a function that maps the given argument lists
  to the given values, specified in vectors like this:

  [[arg1 arg2] value1]
  [[arg3] value2]"

  [& argument-bindings]
  (apply stubs/make-stub argument-bindings))

(defmacro with-stubs
  "Temporarily redefines functions while executing the body.
  The temporary function definitions are specified with a function
  symbol, an argument list and a return value like this:

  (fn1 arg1 arg2) value1
  (fn2 arg3) value2"

  [fn-bindings & body]
  `(with-redefs
     ~(redefs/stub-bindings->redefs fn-bindings)
     ~(cons 'do body)))
