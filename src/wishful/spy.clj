(ns wishful.spy
  (:require [wishful.matchers :as match]))

(declare value-for-args invalid-arguments! record-call
         compute-spy-value calls* arglist-matches?
         spy-spec-matches? spy-spec-value)

(defn make-spy
  [& spy-specs]
  (let [spy-specs (reverse spy-specs)
        calls (atom [])]
    (with-meta
      (fn [& args]
        (record-call
          calls args
          #(compute-spy-value spy-specs args)))
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

(defn- compute-spy-value [spy-specs args]
  (or
    (value-for-args args spy-specs)
    (invalid-arguments! args)))

(defn- value-for-args [actual-args spy-specs]
  (->>
    spy-specs
    (filter #(spy-spec-matches? actual-args %))
    first
    (spy-spec-value actual-args)))

(defn- spy-spec-matches? [args spy-spec]
  (if (fn? spy-spec)
    true
    (every?
      identity
      (map #(match/arg-matches? %1 %2) (first spy-spec) args))))

(defn- spy-spec-value
  [actual-args spy-spec]
  (if (fn? spy-spec)
    (apply spy-spec actual-args)
    (second spy-spec)))

(defn- invalid-arguments! [args]
  (throw
    (IllegalArgumentException.
      (str "No spy provided for arguments: " (apply str args)))))

(defn- calls* [spy-fn] (-> spy-fn meta ::calls))
