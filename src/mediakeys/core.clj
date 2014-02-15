(ns mediakeys.core)
(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])

(defn ^HotKeyListener action-listener [action]
   (proxy [HotKeyListener] []
            (onHotKey [event] (action event))))

(defn make-keystroke [keystroke]
    (KeyStroke/getKeyStroke keystroke))

(def ^Provider current-provider
    (Provider/getCurrentProvider false))

(defn -main [] 
    (.register current-provider 
        (make-keystroke "control 1")
        (action-listener println )))