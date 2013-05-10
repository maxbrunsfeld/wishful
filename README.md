Wishful
=======

[![Build Status](https://travis-ci.org/maxbrunsfeld/wishful.png)](https://travis-ci.org/maxbrunsfeld/wishful)

Simple test spies for clojure.

Contents:
  - [Installation](#installation)
  - [Using Spies](#using-spies)
  - [Argument Matchers](#argument-matchers)
  - [Checking Calls](#checking-calls)
  - [Using with Speclj](#using-with-speclj)

## Installation

Add wishful to your project's dependencies:

```clojure
  [wishful "0.1.1"]
```

## Using Spies

The `with-spies` macro temporarily redefines functions.

```clojure
(use 'wishful.core)

(with-spies
  [(some-fn :arg1) :value1
   (other-fn :arg2 :arg3) :value2]

  (is (= :value1 (some-fn :arg1)))
  (is (= :value3 (other-fn :arg2 :arg3)))))
```

A function can be set to return different values for different arguments.

```clojure
(with-spies
  [(some-fn :arg1) :value1
   (some-fn :arg2) :value2]
  (is (= :value2 (some-fn :arg2))))
```

Later values take precedence over earlier ones, in cases where both
argument lists match.

```clojure
(with-spies
  [(some-fn :arg1) :value1
   (some-fn :arg1) :value2]
  (is (= :value2 (some-fn :arg1))))
```

## Argument Matchers

You can specify arguments more loosely using the `any-arg` function.

```clojure
(with-spies
  [(some-fn (any-arg)) :value1]
  (is (= :value1 (some-fn "anything at all"))))
```

You can pass `any-arg` a predicate function and arguments.

```clojure
(with-spies
  [(some-fn (any-arg)) :value1
   (some-fn (any-arg even?)) :value2
   (some-fn (any-arg > 10)) :value3]
  (is (= :value1 (some-fn 3)))
  (is (= :value2 (some-fn 4)))
  (is (= :value3 (some-fn 11))))
```

## Checking Calls

Spy functions record the arguments with which they're called.

```clojure
(with-spies
  [(some-fn (any-arg)) :value]

  (some-fn 1 2)
  (some-fn 3 4)

  (is (= (calls some-fn)
         [{:args [1 2] :return :value}
          {:args [3 4] :return :value}])))
```

## Using with Speclj

If you're using [speclj](https://github.com/slagyr/speclj), the `spy-on` function
provides a slightly cleaner way to set up spies for the duration of a spec:

```clojure
(use 'speclj.core)
(use 'wishful.speclj)

(describe "spying on functions"
  (spy-on
    (some-fn :arg1) :value1
    (other-fn :arg2) :value2)
    
  (it "works"
    (should= (some-fn :arg1) :value1)
    (should= (some-fn :arg2) :value2))
    
    (describe "overriding outer spies"
      (spy-on
        (some-fn (any-arg)) :yet-another-value)
    
      (it "also works"
        (should= (some-fn :arg1) :yet-another-value))))
```

## License

Copyright © 2013 Max Brunsfeld

Distributed under the Eclipse Public License, the same as Clojure.
