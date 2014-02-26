(ns mediakeys.browser.events
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

(def special-chars {
        32 "space"
        9 "tab"
        37 "left"
        38 "up"
        39 "right"
        40 "down"
    })

(defn from-char-code [code]
    (let [special (special-chars code)]
        (or special
            (.toLowerCase
                (.fromCharCode js/String code)))))
(def characters
    (map< from-char-code 
        (second split-codes)))

(defn subscribe [channel function]
    (go 
        (while true
            (let [thing (<! channel)]
                (function thing)))))
