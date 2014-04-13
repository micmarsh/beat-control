(ns mediakeys.sockets
    (:use [mediakeys.utils :only [defcurried dochan]]
          [clojure.core.async :only [put!]])
    (:import [org.webbitserver WebSocketHandler]))

(defcurried send! [c message] 
    (.send c message))

(defn keypresses [incoming]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (println "yo opened") 
            (dochan incoming (send! c)))
        (onClose [c] (println "closed" c))
        (onMessage [c j] )))

(defn controls [new-keys incoming-changes]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (dochan incoming-changes (send! c)))
        (onClose [c] (println "closed changes" c))
        (onMessage [c message] 
            (println (str "yo message " message))
            (put! new-keys message))))

(defn errors [incoming-errors]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (dochan incoming-errors (send! c)))
        (onClose [c])
        (onMessage[c j])))
