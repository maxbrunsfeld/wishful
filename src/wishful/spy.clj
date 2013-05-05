(ns wishful.spy
  (:require [wishful.matchers :as match]))

(declare value-for-args invalid-arguments! record-call
         compute-spy-value calls* arglist-matches?)

(defn make-spy
  [& arglists-with-values]
  (let [arglists-with-values (reverse arglists-with-values)
        calls (atom [])]
    (with-meta
      (fn [& args]
        (record-call
          calls args
          #(compute-spy-value arglists-with-values args)))
      {::calls calls})))

(defn calls
  [spy-fn]
  @(calls* spy-fn))

(defn reset-calls!
  [spy-fn]
  (swap! (calls* spy-fn) (constantly [])))

(defn- record-call [calls args fn]
  (let [return (fn)]
    (swap! calls conj {:args args :return return})
    return))

(defn- compute-spy-value [arglists-with-values args]
  (or
    (value-for-args args arglists-with-values)
    (invalid-arguments! args)))

(defn- value-for-args [actual-args arglists-with-values]
  (->>
    arglists-with-values
    (filter #(arglist-matches? (first %) actual-args))
    first
    second))

(defn- arglist-matches? [expected-arglist actual-arglist]
  (every?
    identity
    (map #(match/arg-matches? %1 %2) expected-arglist actual-arglist)))

(defn- invalid-arguments! [args]
  (throw
    (IllegalArgumentException.
      (str "No spy provided for arguments: " (apply str args)))))

(defn- calls* [spy-fn] (-> spy-fn meta ::calls))
