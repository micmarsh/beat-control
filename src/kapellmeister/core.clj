(ns kapellmeister.core
    (:use [kapellmeister.hotkeys :only [keypress-events! allowed?]]
          [kapellmeister.sockets :only [keypresses controls errors]]
          [kapellmeister.channels :only [update-keys]]
          [kapellmeister.file :only [SHOULD_WELCOME]]
          [clojure.core.async :only [map< chan split mult]])
    (:require [clojure.data.json :as json])
    (:import 
           [org.webbitserver WebServer WebServers]
           [org.webbitserver.handler StaticFileHandler]
           [com.tulskiy.keymaster.common Provider HotKeyListener]
           [javax.swing KeyStroke])
    (:gen-class))

(defn open [url]
  (->> url
    java.net.URI.
    (.browse (java.awt.Desktop/getDesktop))))

(defn -main []
  (let [incoming-messages (chan)
        incoming-json (map< json/read-json incoming-messages)
        [changes change-errors] (split allowed? incoming-json)
        user-keys (->> changes keypress-events! (map< name))]
  (doto (WebServers/createWebServer 8888)
    (.add "/keypresses" 
      (keypresses (mult user-keys)))
    (.add "/controls"
      (controls incoming-messages 
        (->> update-keys (map< json/write-str) mult)))
    (.add "/errors"
      (errors (->> change-errors (map< json/write-str) mult)))
    (.add (StaticFileHandler. "browser/"))
    (.start))
  (when SHOULD_WELCOME
    (open "http://localhost:8888/welcome.html"))))
