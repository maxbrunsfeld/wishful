(ns wishful.speclj
  (:require [speclj.core]
            [wishful.core]))

(defmacro spy-on
  [& spy-bindings]
  `(speclj.core/around
     [example#]
     (wishful.core/with-spies
       ~spy-bindings
       (example#))))
