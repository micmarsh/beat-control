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

(defn from-char-code [code]
    (cond
        (= code 32)
            "space"
        (= code 9)
            "tab"
        :else 
            (.toLowerCase
                (.fromCharCode js/String code))))
(def characters
    (map< from-char-code 
        (second split-codes)))

(defn subscribe [channel function]
    (go 
        (while true
            (let [thing (<! channel)]
                (function thing)))))

(def print #(.log js/console %))
(subscribe characters print)
(subscribe modifiers print)
