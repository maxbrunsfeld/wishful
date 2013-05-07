(ns wishful.redefs
  (:require [wishful.spy]))

(declare group-by-fn-name transform-each-rhs remove-fn-name-from-forms
         qualify-name)

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
    (group-by #(->> % first first qualify-name))))

(defn qualify-name
  [sym]
  (symbol (str (-> sym resolve meta :ns ns-name) "/" (name sym))))

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
