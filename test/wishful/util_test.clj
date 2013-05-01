(ns wishful.util-test
  (:use clojure.test
        wishful.util))

(def example-bindings
  '[(f1 :arg1 :arg2) :value1
    (f2 :arg3 :arg4) :value2
    (f1 :arg5 :arg6) :value3])

(deftest processing-stub-bindings
  (let [redefs (stub-bindings->redefs example-bindings)
        redefs-map (apply hash-map redefs)]

    (testing "it groups the bindings by their function name"
      (is (= ['f1 'f2] (keys redefs-map))))

    (testing "it maps each fn to a collection of its arglists and their values"
      (is (= '(wishful.util/make-stub
                [[[:arg1 :arg2] :value1]
                 [[:arg5 :arg6] :value3]])
             (get redefs-map 'f1)))

      (is (= '(wishful.util/make-stub [[[:arg3 :arg4] :value2]])
             (get redefs-map 'f2))))))

(deftest test-make-stub
  (testing "when called with args that match an arglist from a stub"
    (let
      [stub (make-stub [[[:arg1 :arg2] :value1]
                        [[:arg3 :arg4] :value2]])]
      (is (= :value1 (stub :arg1 :arg2)))
      (is (= :value2 (stub :arg3 :arg4)))
      (is (= nil (stub :other-arg))))))
