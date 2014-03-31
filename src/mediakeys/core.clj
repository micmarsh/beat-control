(ns mediakeys.core
    (:use [mediakeys.hotkeys :only [keypress-events!]]
          [mediakeys.sockets :only [controls changes]]
          clojure.core.async)
    (:require [clojure.data.json :as json])
    (:import 
           [org.webbitserver WebServer WebServers]
           [org.webbitserver.handler StaticFileHandler]
           [com.tulskiy.keymaster.common Provider HotKeyListener]
           [javax.swing KeyStroke]))

(def incoming-messages (chan))
(def incoming-json 
    (map< json/read-json incoming-messages))

(def prstr (comp println str))
(defn idprint [thing]
    (prstr "received: " thing)
    thing)

(def keypresses (->> incoming-json (map< idprint) keypress-events! (map< name)))

(defn -main []
  (doto (WebServers/createWebServer 8886)
    (.add "/controls" 
      (controls keypresses incoming-messages))
    (.add "/changes"
      (changes { } (mult incoming-messages)))
    (.add (StaticFileHandler. "browser/"))
    (.start)))
