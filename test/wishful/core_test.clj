(ns wishful.core-test
  (:use clojure.test
        wishful.core
        fixtures.functions))

(deftest test-with-stub
  (testing "it redefines a function according to an argument list"
    (with-stubs
      [(fixtures.functions/f1 1 2) :value1]
      (is (= :value1 (f1 1 2)))))

  (testing "it restores the vars' original values afterwards"
    (with-stubs
      [(f1 1 2) :value1])
    (is (= 3 (f1 1 2))))

  (testing "it can redefine multiple functions, with multiple argument lists"
    (with-stubs
      [(f1 1 (inc 1)) :value1
       (f1 2 4) :value2
       (f2 1 2) :value3]

      (is (= :value1 (f1 1 2)))
      (is (= :value2 (f1 2 4)))
      (is (= :value3 (f2 1 2)))))

  (testing "it can deal with wildcard parameters"
    (with-stubs
      [(f1 1 (any-arg)) :value1
       (f1 1 (any-arg even?)) :value2
       (f1 1 (any-arg > 10)) :value3]

      (is (= :value1 (f1 1 1)))
      (is (= :value2 (f1 1 2)))
      (is (= :value3 (f1 1 12)))))

  (testing "it keeps track of functions' call counts"
    (with-stubs
      [(f1 1 2) :value1
       (f1 2 3) :value2]
      (f1 1 2)
      (f1 2 3)
      (is (= 2 (-> f1 calls count)))
      (reset-calls! f1)
      (is (= 0 (-> f1 calls count))))))
