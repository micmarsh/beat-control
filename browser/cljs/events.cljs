(ns events.cljs
    (:use [cljs.core.async :only [chan <! map< split put!]])
    (:use-macros [cljs.core.async.macros :only [go]]))

(def keypresses (chan))

(def modifier-codes {
        16 "shift"
        17 "control"
        18 "alt"
    })

(defn modifier? [code]
    (contains? modifier-codes code))

(.addEventListener js/document "keydown"
    (fn [event]
        (put! keypresses event)))

(def keycodes (map< #(.-keyCode %) keypresses))
(def split-codes (split modifier? keycodes))

(def modifiers
    (map<  modifier-codes
        (first split-codes)))
(def characters
    (map< (.-fromCharCode js/String)
        (second split-codes)))

(defn subscribe [channel function]
    (go 
        (while true
            (let [thing (<! channel)]
                (function thing)))))

(subscribe characters #(.log js/console %))
(subscribe modifiers #(.log js/console %))