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
                    n [(println hotkey)]
                    keystroke [(make-keystroke hotkey)]]
                    (.register provider keystroke
                        (proxy [HotKeyListener] []
                            (onHotKey [event]
                                (.onNext s action))))))))

; schema {:play "hotkey combo", :forward "",...} (doesn't have to be all items, since this is just updates)
(def prstr (comp println str))

(def provider (atom (Provider/getCurrentProvider false)))

(defn keypress-events! [key-changes]
    (.flatMap key-changes 
        (rx/fn [key-update] 
            (let [new-keys (swap! current-keys #(merge % key-update))]
                (.reset @provider)
                (.stop @provider)
                (reset! provider (Provider/getCurrentProvider false))
                (register-keys! @provider new-keys)))))
