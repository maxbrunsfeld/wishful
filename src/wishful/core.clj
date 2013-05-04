(ns wishful.core
  (:require [wishful.redefs :as redefs]
            [wishful.stubs :as stubs]
            [wishful.matchers :as match]))

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

(defn any-arg
  "Creates a matcher which can be used to constrain arguments to stubs.
  With no arguments, returns a matchers that matches everything.
  Examples:

  (any-arg)
  (any-arg even?)
  (any-arg contains? :some-key)"

  ([] (any-arg (constantly true)))
  ([matcher-fn & args]
   (apply match/any-arg (cons matcher-fn args))))

(defn make-stub
  "Returns a function that maps the given argument lists
  to the given values, specified in vectors like this:

  [[arg1 arg2] value1]
  [[arg3] value2]"

  [& argument-bindings]
  (apply stubs/make-stub argument-bindings))

(defn calls
  "Returns the calls to a stub a vector of maps. Each map
  has these keys: [:args, :return, :exception]"
  [stub]
  (stubs/calls stub))

(defn reset-calls!
  "Clears the calls vector for a stub"
  [stub]
  (stubs/reset-calls! stub))
