(ns mediakeys.core
    (:use [mediakeys.hotkeys :only [keypress-events!]]
          mediakeys.rx)
    (:require [clojure.data.json :as json])
    (:import 
           [org.webbitserver WebServer WebServers WebSocketHandler]
           [org.webbitserver.handler StaticFileHandler]))

(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])

(def incoming-sub (atom nil))
(def incoming-observable
    (observable 
        (fn [s]
            (reset! incoming-sub s))))
(def incoming-json 
    (map incoming-observable json/read-json))

(def keypresses (-> incoming-json  keypress-events! (map name)))

(defn -main []
  (doto (WebServers/createWebServer 8080)
    (.add "/controls"
          (proxy [WebSocketHandler] []
            (onOpen [c]
                (println "yo opened") 
                (sub keypresses
                    #(.send c %)))
            (onClose [c] (println "closed" c))
            (onMessage [c j] 
                (next! @incoming-sub j))))
    (.add (StaticFileHandler. "."))
    (.start))
  (sub keypresses println)
  (next! @incoming-sub "{}"))