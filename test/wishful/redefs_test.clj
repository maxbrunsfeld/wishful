(ns wishful.redefs-test
  (:use clojure.test
        wishful.core))

(def example-bindings
  '[(f1 :arg1 :arg2) :value1
    (f2 :arg3 :arg4) :value2
    (f1 :arg5 :arg6) :value3])

(def spy-bindings->redefs #'wishful.core/spy-bindings->redefs)

(deftest processing-spy-bindings
  (let [redefs (spy-bindings->redefs example-bindings)
        redefs-map (apply hash-map redefs)]

    (testing "it groups the bindings by their function name"
      (is (= ['f1 'f2] (keys redefs-map))))

    (testing "it maps each fn to a collection of its arglists and their values"
      (is (= '(wishful.core/make-spy
                [[:arg1 :arg2] :value1]
                [[:arg5 :arg6] :value3])
             (get redefs-map 'f1)))

      (is (= '(wishful.core/make-spy [[:arg3 :arg4] :value2])
             (get redefs-map 'f2))))))

