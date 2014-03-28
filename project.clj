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
                 [com.netflix.rxjava/rxjava-core "0.17.0-RC1"]
                 [com.netflix.rxjava/rxjava-clojure "0.17.0-RC1"]]

  ;   ;; uberjar
  :aot :all
  :omit-source true
  :uberjar-name "hotkeys.jar"


  :main mediakeys.core)