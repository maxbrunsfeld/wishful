Wishful
=======

Wishful is a a small clojure library stubbing, spying, and top-down test-driven design.

## Usage

```clojure
(with-stubs
  [(some-fn arg1) value1
   (some-fn arg2 arg3) value2
   (other-fn arg4) value3]

  (is (= value1 (some-fn arg1)))
  (is (= value2 (some-fn arg2 arg3)))
  (is (= value3 (other-fn arg4)))))
```

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
