(ns wishful.spy-test
  (:use clojure.test
        wishful.core))

(deftest test-make-spy

  (testing "when given argument lists with return values"
    (let [spy (make-spy [[:arg1 :arg2] :value1]
                        [[:arg3 :arg4] :value2])]

      (testing "it returns a value when called with its corresponding arguments"
        (is (= :value1 (spy :arg1 :arg2)))
        (is (= :value2 (spy :arg3 :arg4))))

      (testing "it throws an exception when called with other arguments"
        (is (thrown-with-msg?
              IllegalArgumentException #"No spy .* :other-arg"
              (spy :other-arg))))

      (testing "it keeps track of its calls"
        (reset-calls! spy)
        (spy :arg1 :arg2)
        (spy :arg3 :arg4)

        (is (= [{:args [:arg1 :arg2] :return :value1}
                {:args [:arg3 :arg4] :return :value2}]
               (calls spy)))

        (reset-calls! spy)

        (is (= [] (calls spy))))))

  (testing "when given a function"
    (let [spy (make-spy str
                        [[:arg3 :arg4] :value2])]

      (testing "it calls that function"
        (is (= "some-other-args" (spy "some-" "other-" "args"))))

      (testing "it returns other values if called with the corresponding args"
        (is (= :value2 (spy :arg3 :arg4))))))

  (testing "when given two different return values for the same arg list"
    (let [spy (make-spy [[:arg] :first-value]
                        [[:arg] :later-value])]

      (testing "the later one overrides the earlier one"
        (is (= :later-value (spy :arg))))))

  (testing "it works with falsy return values"
    (let [spy (make-spy [[(any-arg even?)] nil]
                        [[(any-arg odd?)] false])]

      (is (= nil (spy 2)))
      (is (= false (spy 3)))))

  (testing "when given an argument list with wildcard parameters"
    (let [spy (make-spy [[(any-arg) :arg2] :matched-any]
                        [[(any-arg even?) :arg2] :matched-even]
                        [[(any-arg > 5) :arg2] :matched-greater])]

      (testing "it accepts any argument in place of 'any-arg'"
        (is (= :matched-any (spy 1 :arg2))))

      (testing "it matches arguments against a predicate function, with arguments"
        (is (= :matched-even (spy 2 :arg2)))
        (is (= :matched-greater (spy 6 :arg2)))))))
