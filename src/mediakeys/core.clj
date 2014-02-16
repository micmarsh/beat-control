(ns mediakeys.core
    (:use [mediakeys.hotkeys :only [keypress-events!]])
    (:require [rx.lang.clojure.interop :as rx])
    (:import 
           [rx Observable]
           [org.webbitserver WebServer WebServers WebSocketHandler]
           [org.webbitserver.handler StaticFileHandler]))

(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])

(def dose-keys [{:play "control 1" :forward "control 2"}
                {:back "control 4" :play "control 3"}])

(def incoming-sub (atom nil))
(def incoming-observable
    (Observable/create
        (rx/action [^rx.Subscriber s]
            (reset! incoming-sub s))))
;THIS SHOULD ACTUALLY get transformed a bunch
;before getting fed into keypress-events

(defn from-seq [sequence]
    (Observable/create 
        (rx/action [^rx.Subscriber s]
            (doseq [item sequence]
                (.onNext s item)))))

(defn sub-to-keypresses [] 
    (let [changes (from-seq dose-keys)
          presses (keypress-events! changes)]
        (.subscribe presses 
            (rx/action [x]
                (println x)))))

(def keypresses (-> incoming-observable keypress-events!))

(defn -main []
  (doto (WebServers/createWebServer 8080)
    (.add "/websocket"
          (proxy [WebSocketHandler] []
            (onOpen [c] 
                (.subscribe keypresses
                    (rx/action [action]
                        (.send c (name action)))))
            (onClose [c] (println "closed" c))
            (onMessage [c j] 
                (.onNext incoming-sub j))))
    (.add (StaticFileHandler. "."))
    (.start)))