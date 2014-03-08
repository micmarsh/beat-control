(ns mediakeys.core
    (:use [mediakeys.hotkeys :only [keypress-events! key-config!]]
          [mediakeys.sockets :only [controls changes]]
          mediakeys.rx)
    (:require [clojure.data.json :as json])
    (:import 
           [org.webbitserver WebServer WebServers]
           [org.webbitserver.handler StaticFileHandler]
           [com.tulskiy.keymaster.common Provider HotKeyListener]
           [javax.swing KeyStroke]))


(def configs-sub (atom nil))
(def configs-obs
    (observable 
        (fn [s]
            (reset! configs-sub s))))

(def incoming-sub (atom nil))
(def incoming-messages
    (observable 
        (fn [s]
            (reset! incoming-sub s))))
(def incoming-json 
    (map incoming-messages json/read-json))

(def prstr (comp println str))
(defn idprint [thing]
    (prstr "received: " thing)
    thing)

(def keypresses (-> incoming-json (tap #(next! configs-sub %)) 
    (map idprint) keypress-events! (map name)))

(def configs (-> configs-obs key-config! (map json/write-str)))

(defn -main []
  (doto (WebServers/createWebServer 8886)
    (.add "/controls" 
      (controls keypresses incoming-sub))
    (.add "/changes"
      (changes incoming-sub configs))
    (.add (StaticFileHandler. "browser/"))
    (.start)))
