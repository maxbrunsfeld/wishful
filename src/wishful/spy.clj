(ns wishful.spy
  (:require [wishful.matchers :as match]))

(declare invalid-arguments! record-call compute-spy-value
         apply-spy-spec arglist-matches?)

(defn make-spy
  [& spy-specs]
  (let [spy-specs (reverse spy-specs)
        calls (atom [])]
    (with-meta
      (fn [& args]
        (record-call calls args #(compute-spy-value spy-specs args)))
      {::calls calls})))

(defn calls
  [spy-fn]
  @(-> spy-fn meta ::calls))

(defn reset-calls!
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

(defn apply-spy-spec
  [spy-spec args]
  (if (fn? spy-spec)
    {:matches? true
     :value (apply spy-spec args)}
    {:matches? (arglist-matches? (first spy-spec) args)
     :value (second spy-spec)}))

(defn- arglist-matches? [arglist actual-args]
  (every? identity (map match/arg-matches? arglist actual-args)))

(defn- invalid-arguments! [args]
  (throw
    (IllegalArgumentException.
      (str "No spy provided for arguments: " (apply str args)))))
