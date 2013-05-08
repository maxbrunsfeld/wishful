(ns wishful.speclj-test
  (:use wishful.core
        wishful.speclj
        speclj.core))

(describe "spy-on"
  (spy-on
    (type (any-arg vector?)) 'short-vector
    (type (any-arg #(-> % count (> 3)))) 'long-vector)

  (it "redefines functions"
    (should= (type []) 'short-vector)
    (should= (type [1 2 3 4]) 'long-vector)
    (should= (-> type calls count) 2))

  (describe "defining spies in nested suites"
    (spy-on
      (type (any-arg vector?)) 'really-short-vector
      (type (any-arg #(-> % count (> 3)))) 'really-long-vector)

    (it "overrides the outer spies"
      (should= (type []) 'really-short-vector)
      (should= (type [1 2 3 4]) 'really-long-vector)
      (should= (-> type calls count) 2)))

  (describe "after overriding spies"
    (it "uses the outer spies"
      (should= (type []) 'short-vector)
      (should= (type [1 2 3 4]) 'long-vector)
      (should= (-> type calls count) 2))))
