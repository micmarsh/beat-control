(ns mediakeys.sockets
    (:use [mediakeys.utils :only [defcurried]]
        mediakeys.rx)
    (:require [clojure.data.json :as json])
    (:import [org.webbitserver WebSocketHandler]))

(defcurried send [c message] 
    (.send c message))

(defn controls [keypresses incoming-sub]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (println "yo opened") 
            (sub keypresses (send c))
            (next! @incoming-sub "{}"))
        (onClose [c] (println "closed" c))
        (onMessage [c j] )))

(defn changes [current-keys incoming-sub]
    (proxy [WebSocketHandler] []
        (onOpen [c] 
            (send c (json/write-str @current-keys))) 
        (onClose [c] (println "closed changes" c))
        (onMessage [c j] 
            (println (str "yo message " j))
            (next! @incoming-sub j))))
