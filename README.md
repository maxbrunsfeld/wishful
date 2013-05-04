Wishful
=======

Wishful is a small clojure library for stubbing, spying, and top-down TDD.

Contents:
  - [Installation](#installation)
  - [Stubbing](#stubbing)
  - [Argument Matchers](#argument-matchers)
  - [Checking Calls](#checking-calls)

## Installation

Add wishful to your project's dependencies:

```clojure
  [wishful "0.1.0-SNAPSHOT"]
```

## Stubbing

The `with-stubs` macro temporarily redefines functions.

```clojure
(use 'wishful.core)

(with-stubs
  [(some-fn :arg1) :value1
   (other-fn :arg2 :arg3) :value2]
   
  (is (= :value1 (some-fn :arg1)))
  (is (= :value3 (other-fn :arg2 :arg3)))))
```

A function can be stubbed to return different values for different arguments.

```clojure
(with-stubs
  [(some-fn :arg1) :value1
   (some-fn :arg2) :value2]
  (is (= :value2 (some-fn :arg2))))
```

Later stubs take precedence over earlier ones.

```clojure
(with-stubs
  [(some-fn :arg1) :value1
   (some-fn :arg1) :value2]
  (is (= :value2 (some-fn :arg1))))
```

## Argument Matchers

You can specify arguments more loosely using the `any-arg` function.

```clojure
(with-stubs
  [(some-fn (any-arg)) :value1]
  (is (= :value1 (some-fn "anything at all"))))
```

You can pass `any-arg` a predicate function and arguments.

```clojure
(with-stubs
  [(some-fn (any-arg)) :value1
   (some-fn (any-arg even?)) :value2
   (some-fn (any-arg > 10)) :value3]
  (is (= :value1 (some-fn 3)))
  (is (= :value2 (some-fn 4)))
  (is (= :value3 (some-fn 11))))
```

## Checking Calls

Stub functions record the arguments with which they're called.

```clojure
(with-stubs
  [(some-fn (any-arg)) :value]

  (some-fn 1 2)
  (some-fn 3 4)
  
  (is (= (calls some-fn)
         [{:args [1 2]} {:args [3 4]}])))
```

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
