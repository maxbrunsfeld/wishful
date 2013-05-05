(ns wishful.redefs
  (:require [wishful.spy]))

(declare group-by-fn-name transform-each-rhs remove-fn-name-from-forms)

(defn spy-bindings->redefs
  [spy-bindings]
  (->>
    spy-bindings
    group-by-fn-name
    (transform-each-rhs
      #(->> %
            remove-fn-name-from-forms
            vec
            (apply list 'wishful.spy/make-spy)))))

(defn- group-by-fn-name
  [bindings]
  (->>
    bindings
    (partition 2)
    (group-by (comp first first))))

(defn- transform-each-rhs
  [f coll]
  (mapcat
    (fn [[k v]] [k (f v)])
    coll))

(defn- remove-fn-name-from-forms
  [bindings]
  (map
    (fn [[form val]] [(vec (rest form)) val])
    bindings))
