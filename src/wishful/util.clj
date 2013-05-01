(ns wishful.util)

(declare group-by-fn-name transform-each-rhs remove-fn-name-from-forms value-for-args invalid-arguments!)

(defn make-stub
  [arglists-with-values]
  (let [prioritized-arglists-with-values (reverse arglists-with-values)]
    (fn [& args]
      (or
        (value-for-args args prioritized-arglists-with-values)
        (invalid-arguments! args)))))

(defn- value-for-args [actual-args arglists-with-values]
  (->>
    arglists-with-values
    (filter #(= (first %) actual-args))
    first
    second))

(defn- invalid-arguments! [args]
  (throw
    (IllegalArgumentException.
      (str "No stub provided for arguments: " (apply str args)))))

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
