(in-ns 'wishful.core)

(declare invalid-arguments! record-call compute-spy-value
         apply-spy-spec arglist-matches?)

(defn make-spy
  "Returns a function that maps the given argument lists
  to the given values, specified in vectors like this:

  [[arg1 arg2] value1]
  [[arg3] value2]"
  [& spy-specs]
  (let [spy-specs (reverse spy-specs)
        calls (atom [])]
    (with-meta
      (fn [& args]
        (record-call calls args #(compute-spy-value spy-specs args)))
      {::calls calls})))

(defn any-arg
  "Creates a matcher which can be used to constrain arguments to spies.
  With no arguments, returns a matchers that matches everything.
  Examples:

  (any-arg)
  (any-arg even?)
  (any-arg contains? :some-key)"
  ([] (any-arg (constantly true)))
  ([f & args]
   {::matcher? true :function f :args args}))

(defn calls
  "Returns the calls to a spy as a vector of maps. Each map
  will have two keys: :args and either :return or :exception"
  [spy-fn]
  @(-> spy-fn meta ::calls))

(defn reset-calls!
  "Clears the calls vector for a spy"
  [spy-fn]
  (swap! (-> spy-fn meta ::calls) (constantly [])))

(defn- record-call [calls args fn]
  (let [return (fn)]
    (swap! calls conj {:args args :return return})
    return))

(defn- compute-spy-value [spy-specs args]
  (or (->>
        spy-specs
        (map #(apply-spy-spec % args))
        (filter :matches?)
        first
        :value)
      (invalid-arguments! args)))

(defn- apply-spy-spec
  [spy-spec args]
  (if (fn? spy-spec)
    {:matches? true
     :value (apply spy-spec args)}
    {:matches? (arglist-matches? (first spy-spec) args)
     :value (second spy-spec)}))

(defn- arg-matches?
  [expected actual]
  (if (::matcher? expected)
    (apply (:function expected) (cons actual (:args expected)))
    (= expected actual)))

(defn- arglist-matches? [arglist actual-args]
  (every? identity (map arg-matches? arglist actual-args)))

(defn- invalid-arguments! [args]
  (throw
    (IllegalArgumentException.
      (str "No spy provided for arguments: " (apply str args)))))
