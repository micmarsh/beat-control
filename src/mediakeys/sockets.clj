(ns mediakeys.sockets
    (:use [mediakeys.utils :only [defcurried]]
        mediakeys.rx)
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

(defn changes [incoming-sub]
    (proxy [WebSocketHandler] []
        (onOpen [c] (println "changes")) 
        (onClose [c] (println "closed changes" c))
        (onMessage [c j] 
            (println (str "yo message " j))
            (next! @incoming-sub j))))

(defn see-config [configs]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (sub configs (send c)))
        (onClose [c] (println "closed" c))
        (onMessage [c j] (println (str "yo mesage " j)))))

