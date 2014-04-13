(ns mediakeys.sockets
    (:use [mediakeys.utils :only [defcurried dochan]]
          [clojure.core.async :only [put! mult]])
    (:import [org.webbitserver WebSocketHandler]))

(defcurried send! [c message] 
    (println "sending"  message)
    (.send c message))


(defn subcopy [channel function]
    (dochan (mult channel) function))

(defn keypresses [incoming]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (subcopy incoming (send! c)))
        (onClose [c] (println "closed" c))
        (onMessage [c j] )))

(defn controls [new-keys incoming-changes]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (subcopy incoming-changes (send! c)))
        (onClose [c] (println "closed changes" c))
        (onMessage [c message] 
            (put! new-keys message))))

(defn errors [incoming-errors]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (subcopy incoming-errors (send! c)))
        (onClose [c])
        (onMessage[c j])))
