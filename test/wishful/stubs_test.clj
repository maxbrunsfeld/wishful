(ns wishful.stubs-test
  (:use clojure.test
        wishful.stubs))

(deftest test-make-stub
  (testing "when called with args that match an arglist from a stub"
    (testing "it returns the corresponding value"
      (let
        [stub (make-stub [[[:arg1 :arg2] :value1]
                          [[:arg3 :arg4] :value2]])]
        (is (= :value1 (stub :arg1 :arg2)))
        (is (= :value2 (stub :arg3 :arg4))))))

  (testing "when called with args that don't match an arglist from the stub"
    (testing "it throws an exception"
      (let
        [stub (make-stub [[[:arg] :value]])]
        (is (thrown-with-msg?
              IllegalArgumentException #"No stub .* :other-arg"
              (stub :other-arg))))))

  (testing "when given two different return values for the same arg list"
    (testing "the later one overrides the earlier one"
      (let
        [stub (make-stub [[[:arg] :first-value]
                          [[:arg] :later-value]])]
        (is (= :later-value (stub :arg)))))))
