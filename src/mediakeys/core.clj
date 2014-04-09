(ns mediakeys.core
    (:use [mediakeys.hotkeys :only [keypress-events!]]
          [mediakeys.sockets :only [keypresses controls errors]]
          [mediakeys.channels :only [update-keys keymaster-errors]]
          [clojure.core.async :only [map< chan]])
    (:require [clojure.data.json :as json])
    (:import 
           [org.webbitserver WebServer WebServers]
           [org.webbitserver.handler StaticFileHandler]
           [com.tulskiy.keymaster.common Provider HotKeyListener]
           [javax.swing KeyStroke])
    (:gen-class))

(defn -main []
  (let [incoming-messages (chan) 
        user-keys (->> (map< json/read-json) keypress-events! (map< name))]
  (doto (WebServers/createWebServer 8886)
    (.add "/keypresses" 
      (keypresses user-keys))
    (.add "/controls"
      (controls incoming-messages update-keys))
    (.add "/errors"
      (errors keymaster-errors))
    (.add (StaticFileHandler. "browser/"))
    (.start)))
