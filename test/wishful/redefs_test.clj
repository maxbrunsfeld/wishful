(ns wishful.redefs-test
  (:use clojure.test
        wishful.core))

(def example-bindings
  '[(f1 :arg1 :arg2) :value1
    (f2 :arg3 :arg4) :value2
    (f1 :arg5 :arg6) :value3])

(def spy-bindings->redefs #'wishful.core/spy-bindings->redefs)

(deftest processing-spy-bindings
  (testing "when given arglists and values"
    (let [example-bindings '[(f1 :arg1 :arg2) :value1
                             (f2 :arg3 :arg4) :value2
                             (f1 :arg5 :arg6) :value3]]

      (testing "it maps each fn to a spy with the right arglists and values"
        (is (= ['f1 '(wishful.core/make-spy
                       [[:arg1 :arg2] :value1]
                       [[:arg5 :arg6] :value3])
                'f2 '(wishful.core/make-spy
                       [[:arg3 :arg4] :value2])]

               (spy-bindings->redefs example-bindings))))))

  (testing "when given functions"
    (let [example-bindings '[f1 println
                             (f2 :arg3 :arg4) :value2
                             (f1 :arg5 :arg6) :value3]]

      (testing "it maps each fn to a spy with the right arglists and values"
        (is (= ['f1 '(wishful.core/make-spy
                       println
                       [[:arg5 :arg6] :value3])
                'f2 '(wishful.core/make-spy
                       [[:arg3 :arg4] :value2])]

               (spy-bindings->redefs example-bindings)))))))

