(ns mediakeys.sockets
    (:use [mediakeys.utils :only [defcurried dochan]]
          [clojure.core.async :only [put! chan close! tap]])
    (:import [org.webbitserver WebSocketHandler]))

(defcurried send! [c message] 
    (.send c message))

(defn clone [mult]
    (let [return (chan)]
        (tap mult return)
        return))

(def channels (atom { }))
(defn start! [conn channel]
    (swap! channels assoc conn channel)
    (print @channels))
(defn stop! [conn]
    (close! (@channels conn))
    (swap! channels dissoc conn)
    (print @channels))

(defn keypresses [incoming]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (let [channel (clone incoming)]
                (dochan channel (send! c))
                (start! c channel)))
        (onClose [c] 
            (println "closed" c)
            (stop! c))
        (onMessage [c j] )))

(defn controls [new-keys incoming-changes]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (let [channel (clone incoming-changes)]
                (dochan channel (send! c))
                (start! c channel)))
        (onClose [c] 
            (println "closed changes" c)
            (stop! c))
        (onMessage [c message] 
            (put! new-keys message))))

(defn errors [incoming-errors]
    (proxy [WebSocketHandler] []
        (onOpen [c]
           (let [channel (clone incoming-errors)]
                (dochan channel (send! c))
                (start! c channel)))
        (onClose [c]
            (stop! c))
        (onMessage[c j])))
