(ns wishful.stubs)

(declare value-for-args invalid-arguments! record-calls compute-stub-value calls*)

(defn make-stub
  [arglists-with-values]
  (let [arglists-with-values (reverse arglists-with-values)
        calls (atom [])]
    (with-meta
      (fn [& args]
        (record-calls
          calls args
          #(compute-stub-value arglists-with-values args)))
      {::calls calls})))

(defn calls
  [stub-fn]
  @(calls* stub-fn))

(defn reset-stub
  [stub-fn]
  (swap! (calls* stub-fn) (constantly [])))

(defn- record-calls [calls args fn]
  (swap! calls conj {:args args})
  (fn))

(defn- compute-stub-value [arglists-with-values args]
  (or
    (value-for-args args arglists-with-values)
    (invalid-arguments! args)))

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

(defn- calls* [stub-fn] (-> stub-fn meta ::calls))
