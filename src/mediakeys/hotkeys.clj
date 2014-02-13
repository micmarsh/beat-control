(ns mediakeys.hotkeys)
(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])

(defn ^HotKeyListener make-action-listener [action]
   (proxy [HotKeyListener] []
            (onHotKey [event] (action))))

(defn make-keystroke [keystroke]
    (KeyStroke/getKeyStroke keystroke))

(def ^Provider current-provider
    (Provider/getCurrentProvider false))






; schema {:play "hotkey combo", :forward "",...} (doesn't have to be all items, since this is just updates)
(defn key-events [key-changes])
; idea here: key-changes is a channel/observable that's receiving messages
; telling it to change up the hotkeys. for each event from key-changes (filtering erroneous ones at a lower level )
; reset both some provider atom and record of what's what atom (<- which can save to the fs on a separate thread)
; on each keypress in the provider, put something in the channel that this is returning. cool


