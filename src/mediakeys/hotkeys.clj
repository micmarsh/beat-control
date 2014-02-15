(ns mediakeys.hotkeys
    (:require [rx.lang.clojure.interop :as rx])
    (:import [rx Observable])
    (:use [mediakeys.file :only [DEFAULT_KEYS]]))

(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])

; (defn ^HotKeyListener action-listener [action]
;    (proxy [HotKeyListener] []
;             (onHotKey [event] (action event))))

(defn make-keystroke [keystroke]
    (KeyStroke/getKeyStroke keystroke))

(def current-keys (atom DEFAULT_KEYS))

(defn register-keys! [provider keys]
    (Observable/create 
        (rx/action [^rx.Subscriber s]
            (doseq [[action hotkey] keys
                    keystroke [(make-keystroke hotkey)]]
                    (.register provider keystroke
                        (proxy [HotKeyListener] []
                            (onHotKey [event]
                                (.onNext s action))))))))

; schema {:play "hotkey combo", :forward "",...} (doesn't have to be all items, since this is just updates)
(def key-events 
    (let [provider (Provider/getCurrentProvider false)]
        (fn [key-changes]
            )))



