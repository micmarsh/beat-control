(defproject mediakeys "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-bin "0.3.4"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.2.4"]
                 [org.webbitserver/webbit "0.4.3"]
                 [com.github.tulskiy/jkeymaster "1.1"]
                 [org.clojure/core.async "0.1.278.0-76b25b-alpha"]]

    ;; uberjar
  :uberjar-name "hotkeys.jar"
  :aot :all
  :omit-source true

  :main mediakeys.core)