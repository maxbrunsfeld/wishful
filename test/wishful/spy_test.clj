(ns wishful.spy-test
  (:use clojure.test
        wishful.spy
        wishful.matchers))

(deftest test-make-spy
  (testing "when called with args that match an arglist from a spy"
    (testing "it returns the corresponding value"
      (let
        [spy (make-spy [[:arg1 :arg2] :value1]
                         [[:arg3 :arg4] :value2])]
        (is (= :value1 (spy :arg1 :arg2)))
        (is (= :value2 (spy :arg3 :arg4))))))

  (testing "when called with args that don't match an arglist from the spy"
    (testing "it throws an exception"
      (let
        [spy (make-spy [[:arg] :value])]
        (is (thrown-with-msg?
              IllegalArgumentException #"No spy .* :other-arg"
              (spy :other-arg))))))

  (testing "when given two different return values for the same arg list"
    (testing "the later one overrides the earlier one"
      (let
        [spy (make-spy [[:arg] :first-value]
                         [[:arg] :later-value])]
        (is (= :later-value (spy :arg))))))

  (testing "it handles wildcard parameters"
    (let
      [spy (make-spy [[(any-arg even?) :arg2] :other]
                       [[:arg1 (any-arg < 5)] :smaller]
                       [[:arg1 (any-arg > 5)] :greater])]
      (is (= :other (spy 2 :arg2)))
      (is (= :smaller (spy :arg1 3)))
      (is (= :greater (spy :arg1 6)))))

  (testing "it keeps track of its calls"
    (let
      [spy (make-spy [[:arg1] :value1]
                       [[:arg2] :value2])]
      (spy :arg1)
      (spy :arg2)

      (is (= 2 (-> spy calls count)))
      (is (= {:args [:arg1] :return :value1} (-> spy calls first)))
      (is (= {:args [:arg2] :return :value2} (-> spy calls second)))

      (reset-calls! spy)

      (is (= 0 (-> spy calls count))))))
