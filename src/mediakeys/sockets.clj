(ns mediakeys.sockets
    (:use [mediakeys.utils :only [defcurried]]
        clojure.core.async)
    (:require [clojure.data.json :as json])
    (:import [org.webbitserver WebSocketHandler]))

(defcurried send [c message] 
    (.send c message))

(defn controls [keypresses incoming-sub]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (println "yo opened") 
            (map< (send c) keypresses)
            (put! incoming-sub "{}"))
        (onClose [c] (println "closed" c))
        (onMessage [c j] )))

(defn changes [current-keys incoming-sub]
    (proxy [WebSocketHandler] []
        (onOpen [c] 
            (send c (json/write-str "{}"))) 
        (onClose [c] (println "closed changes" c))
        (onMessage [c message] 
            (println (str "yo message " message))
            (put! incoming-sub message))))
