(ns wishful.redefs-test
  (:use clojure.test
        wishful.redefs
        fixtures.functions))

(def example-bindings
  '[(f1 :arg1 :arg2) :value1
    (f2 :arg3 :arg4) :value2
    (fixtures.functions/f1 :arg5 :arg6) :value3])

(deftest processing-spy-bindings
  (let [redefs (spy-bindings->redefs example-bindings)
        redefs-map (apply hash-map redefs)]

    (testing "it groups the bindings by their function name"
      (is (= ['fixtures.functions/f1 'fixtures.functions/f2] (keys redefs-map))))

    (testing "it maps each fn to a collection of its arglists and their values"
      (is (= '(wishful.spy/make-spy
                [[:arg1 :arg2] :value1]
                [[:arg5 :arg6] :value3])
             (get redefs-map 'fixtures.functions/f1)))

      (is (= '(wishful.spy/make-spy [[:arg3 :arg4] :value2])
             (get redefs-map 'fixtures.functions/f2))))))
