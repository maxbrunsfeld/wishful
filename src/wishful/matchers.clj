(ns wishful.matchers)

(defn any-arg
  ([] (any-arg (constantly true)))
  ([f & args]
   {::is-matcher? true :function f :args args}))

(defmulti arg-matches?
  (fn [expected actual]
    (and (map? expected) (::is-matcher? expected))))

(defmethod arg-matches? :default [expected actual] (= expected actual))
(defmethod arg-matches? true
  [expected actual]
  (apply (:function expected) (cons actual (:args expected))))
