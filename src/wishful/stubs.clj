(ns wishful.stubs)

(declare value-for-args invalid-arguments!)

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
