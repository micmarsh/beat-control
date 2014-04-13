(ns mediakeys.sockets
    (:use [mediakeys.utils :only [defcurried dochan]]
          [clojure.core.async :only [put! chan tap]])
    (:import [org.webbitserver WebSocketHandler]))

(defcurried send! [c message] 
    (.send c message))

(defn clone [mult]
    (let [return (chan)]
        (tap mult return)
        return))

(defn keypresses [incoming]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (dochan (clone incoming) (send! c)))
        (onClose [c] (println "closed" c))
        (onMessage [c j] )))

(defn controls [new-keys incoming-changes]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (dochan (clone incoming-changes) (send! c)))
        (onClose [c] (println "closed changes" c))
        (onMessage [c message] 
            (put! new-keys message))))

(defn errors [incoming-errors]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (dochan (clone incoming-errors) (send! c)))
        (onClose [c])
        (onMessage[c j])))
