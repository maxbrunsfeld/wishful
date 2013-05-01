(ns wishful.util)

(declare group-by-fn-name transform-each-rhs remove-fn-name-from-forms)

(defn make-stub
  [arglists-with-values]
  (fn [& args]
    (->>
      arglists-with-values
      (filter #(= (first %) args))
      first
      second)))

(defn stub-bindings->redefs
  [stub-bindings]
  (->>
    stub-bindings
    group-by-fn-name
    (transform-each-rhs
      #(->> %
            remove-fn-name-from-forms
            vec
            (list 'wishful.util/make-stub)))))

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
