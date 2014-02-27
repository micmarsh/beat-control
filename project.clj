(defproject mediakeys "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "1.0.2"]]
  :dependencies [[reagent "0.4.1"]
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.2.4"]
                 [org.webbitserver/webbit "0.4.3"]
                 [com.github.tulskiy/jkeymaster "1.1"]
                 [org.clojure/clojurescript "0.0-2173"]
                 [com.netflix.rxjava/rxjava-core "0.17.0-RC1"]
                 [com.netflix.rxjava/rxjava-clojure "0.17.0-RC1"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]]
  :main mediakeys.core
  :cljsbuild {
    :builds [{
        ; The path to the top-level ClojureScript source directory:
        :source-paths ["browser/cljs" ]
        :compiler {
          :preamble ["reagent/react.min.js"]
          :output-to "browser/javascripts/main.js"  
          :optimizations :whitespace
          :pretty-print true}}]})