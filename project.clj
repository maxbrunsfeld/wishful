(defproject wishful "0.1.2"
  :description "Simple test spies and stubs for clojure"
  :url "https://github.com/maxbrunsfeld/wishful"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :scm {:name "git"
        :url "https://github.com/maxbrunsfeld/wishful"}
  :profiles {:dev {:dependencies [[speclj "2.5.0"]]
                   :plugins [[speclj "2.5.0"]]}})
