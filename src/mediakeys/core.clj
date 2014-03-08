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


(def config-sub (atom nil))
(def config-json
    (observable 
        (fn [s]
            (reset! config-sub s))))

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

(def keypresses (-> incoming-json (tap #(next! @config-sub %)) 
    (map idprint) keypress-events! (map name)))

(def configs (-> config-json (map idprint) key-config! (map json/write-str)))

(defn -main []
  (doto (WebServers/createWebServer 8886)
    (.add "/controls" 
      (controls keypresses incoming-sub))
    (.add "/changes"
      (changes incoming-sub configs))
    (.add (StaticFileHandler. "browser/"))
    (.start)))
