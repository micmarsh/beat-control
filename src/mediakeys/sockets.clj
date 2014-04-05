(ns mediakeys.sockets
    (:use [mediakeys.utils :only [defcurried dochan]]
          [clojure.core.async :only [put!]])
    (:require [clojure.data.json :as json])
    (:import [org.webbitserver WebSocketHandler]))

(defcurried send! [c message] 
    (.send c message))

; TODO rename these routes! "controls" should be keypresses, since that's what
; we're actually listening for, "changes" should be called controls should somehow standardize
; all of the channel deps these have.
; Also, move "seeding" of hotkey stuff into hotkeys, where it belongs

(defn keypresses [incoming]
    (proxy [WebSocketHandler] []
        (onOpen [c]
            (println "yo opened") 
            (dochan incoming (send! c)))
        (onClose [c] (println "closed" c))
        (onMessage [c j] )))

(defn changes [current-keys incoming-sub]
    ; TODO this (or the god controller above should just subscribe
    ; to a "keys-changed" channel you define in file.clj)
    (proxy [WebSocketHandler] []
        (onOpen [c] 
            (send! c (json/write-str "{}"))) 
        (onClose [c] (println "closed changes" c))
        (onMessage [c message] 
            (println (str "yo message " message))
            (put! incoming-sub message))))
