(ns mediakeys.core
    (:use [mediakeys.hotkeys :only [keypress-events!]]
          [mediakeys.sockets :only [keypresses controls]]
          [mediakeys.file :only [current-keys]]
          [clojure.core.async :only [map< chan]])
    (:require [clojure.data.json :as json])
    (:import 
           [org.webbitserver WebServer WebServers]
           [org.webbitserver.handler StaticFileHandler]
           [com.tulskiy.keymaster.common Provider HotKeyListener]
           [javax.swing KeyStroke])
    (:gen-class))

(def incoming-messages (chan))
(def incoming-json 
    (map< json/read-json incoming-messages))

(def prstr (comp println str))
(defn idprint [thing]
    (prstr "received: " thing)
    thing)

(def user-keys (->> incoming-json (map< idprint) keypress-events! (map< name)))

(defn -main []
  (doto (WebServers/createWebServer 8886)
    (.add "/keypresses" 
      (keypresses user-keys))
    (.add "/controls"
      (controls incoming-messages current-keys))
    (.add (StaticFileHandler. "browser/"))
    (.start)))
