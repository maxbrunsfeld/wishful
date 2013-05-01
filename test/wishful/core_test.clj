(ns wishful.core-test
  (:use clojure.test
        wishful.core))

(def f1 +)
(def f2 *)

(deftest test-with-stub
  (testing "it redefines a function according to an argument list"
    (with-stubs
      [(f1 1 2) :stub1]
      (is (= :stub1 (f1 1 2)))))

  (testing "it restores the vars' original values afterwards"
    (with-stubs
      [(f1 1 2) :stub1])
    (is (= 3 (f1 1 2))))

  (testing "it can redefine multiple functions, with multiple argument lists"
    (with-stubs
      [(f1 1 (inc 1)) :stub1
       (f1 2 4) :stub2
       (f2 1 2) :stub3]

      (is (= :stub1 (f1 1 2)))
      (is (= :stub2 (f1 2 4)))
      (is (= :stub3 (f2 1 2)))))
  )
