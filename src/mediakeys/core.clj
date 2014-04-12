(ns mediakeys.core
    (:use [mediakeys.hotkeys :only [keypress-events! allowed?]]
          [mediakeys.sockets :only [keypresses controls errors]]
          [mediakeys.channels :only [update-keys]]
          [clojure.core.async :only [map< chan split]])
    (:require [clojure.data.json :as json])
    (:import 
           [org.webbitserver WebServer WebServers]
           [org.webbitserver.handler StaticFileHandler]
           [com.tulskiy.keymaster.common Provider HotKeyListener]
           [javax.swing KeyStroke])
    (:gen-class))

(defn -main []
  (let [incoming-messages (chan)
        incoming-json (map< json/read-json incoming-messages)
        [changes change-errors] (split allowed? incoming-json)
        user-keys (keypress-events! changes)]
  (doto (WebServers/createWebServer 8886)
    (.add "/keypresses" 
      (keypresses user-keys))
    (.add "/controls"
      (controls incoming-messages update-keys))
    (.add "/errors"
      (errors change-errors))
    (.add (StaticFileHandler. "browser/"))
    (.start))))
