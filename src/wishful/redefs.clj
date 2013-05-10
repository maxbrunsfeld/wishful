(in-ns 'wishful.core)

(declare spy-bindings->redefs group-by-fn-name transform-each-rhs
         remove-fn-name-from-forms get-spy-binding-fn)

(defmacro with-spies
  "Temporarily redefines functions while executing the body.
  The temporary function definitions are specified with a function
  symbol, an argument list and a return value like this:

  [(fn1 arg1 arg2) value1
   (fn2 arg3) value2]"
  [fn-bindings & body]
  `(with-redefs
     ~(spy-bindings->redefs fn-bindings)
     ~(cons 'do body)))

(defn- spy-bindings->redefs
  [spy-bindings]
  (->>
    spy-bindings
    group-by-fn-name
    (transform-each-rhs
      #(->> %
            remove-fn-name-from-forms
            vec
            (apply list 'wishful.core/make-spy)))))

(defn- group-by-fn-name
  [bindings]
  (->>
    bindings
    (partition 2)
    (group-by (comp get-spy-binding-fn first))))

(defn- get-spy-binding-fn
  [spy-binding]
  (if (seq? spy-binding)
    (first spy-binding)
    spy-binding))

(defn- transform-each-rhs
  [f coll]
  (mapcat
    (fn [[k v]] [k (f v)])
    coll))

(defn- remove-fn-name-from-forms
  [bindings]
  (map
    (fn [[form val]]
      (if (list? form)
        [(vec (rest form)) val]
        val))
    bindings))
