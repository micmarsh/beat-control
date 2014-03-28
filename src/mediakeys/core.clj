(ns mediakeys.core
    (:use [mediakeys.hotkeys :only [keypress-events! key-config! current-keys]]
          [mediakeys.sockets :only [controls changes]]
          mediakeys.rx)
    (:require [clojure.data.json :as json])
    (:import 
           [org.webbitserver WebServer WebServers]
           [org.webbitserver.handler StaticFileHandler]
           [com.tulskiy.keymaster.common Provider HotKeyListener]
           [javax.swing KeyStroke])
    (:gen-class))

(def incoming-sub (atom nil))
(def incoming-messages
    (observable 
        (fn [s]
            (reset! incoming-sub s))))
(def incoming-json 
    (rmap incoming-messages json/read-json))

(def prstr (comp println str))
(defn idprint [thing]
    (prstr "received: " thing)
    thing)

(def keypresses (-> incoming-json (rmap idprint) keypress-events! (rmap name)))

(defn -main []
  (doto (WebServers/createWebServer 8886)
    (.add "/controls" 
      (controls keypresses incoming-sub))
    (.add "/changes"
      (changes current-keys incoming-sub))
    (.add (StaticFileHandler. "browser/"))
    (.start)))
