(ns wishful.redefs
  (:require [wishful.stubs]))

(declare group-by-fn-name transform-each-rhs remove-fn-name-from-forms)

(defn stub-bindings->redefs
  [stub-bindings]
  (->>
    stub-bindings
    group-by-fn-name
    (transform-each-rhs
      #(->> %
            remove-fn-name-from-forms
            vec
            (apply list 'wishful.stubs/make-stub)))))

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
