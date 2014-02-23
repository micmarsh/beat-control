(ns mediakeys.core
    (:use [mediakeys.hotkeys :only [keypress-events! key-config!]]
          [mediakeys.sockets :only [controls see-config]]
          mediakeys.rx)
    (:require [clojure.data.json :as json])
    (:import 
           [org.webbitserver WebServer WebServers]
           [org.webbitserver.handler StaticFileHandler]))

(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])

(def incoming-sub (atom nil))
(def incoming-messages
    (observable 
        (fn [s]
            (reset! incoming-sub s))))
(def incoming-json 
    (map incoming-messages json/read-json))

(def keypresses (-> incoming-json  keypress-events! (map name)))
(def configs (-> incoming-json key-config!))

(defn -main []
  (doto (WebServers/createWebServer 8886)
    (.add "/controls" 
      (controls keypresses incoming-sub))
    (.add "/config"
      (see-config configs))
    (.add (StaticFileHandler. "browser/"))
    (.start)))
