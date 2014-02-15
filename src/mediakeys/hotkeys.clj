(ns mediakeys.hotkeys
    (:use [mediakeys.file :only [DEFAULT_KEYS]]
          [clojure.core.async :only [chan put!]]))
(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])

; (defn ^HotKeyListener action-listener [action]
;    (proxy [HotKeyListener] []
;             (onHotKey [event] (action event))))

(defn make-keystroke [keystroke]
    (KeyStroke/getKeyStroke keystroke))

(def current-keys (atom DEFAULT_KEYS))

(defn register-keys! [provider keys]
    (let [keypresses (chan)]
        (doseq [[action hotkey] keys
                keystroke [(make-keystroke hotkey)]]
            (.register provider keystroke
                (proxy [HotKeyListener] []
                    (onHotKey [event]
                        (put! keypresses action)))))
        keypresses))

; schema {:play "hotkey combo", :forward "",...} (doesn't have to be all items, since this is just updates)
(def key-events 
    (let [provider (Provider/getCurrentProvider false)]))



