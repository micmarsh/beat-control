(ns mediakeys.browser.events
    (:require [rx-cljs.observable :as rx])
    (:use [mediakeys.browser.rx :only [multi-sub]]))


(def modifier-codes {
        16 "shift"
        17 "control"
        18 "alt"
    })

(defn modifier? [code]
    (contains? modifier-codes code))


(def keyevents 
    (rx/create (fn [sub]
        (.addEventListener js/document "keydown"
            (fn [event]
                (rx/on-next sub event))))))
(def keycodes 
    (rx/map keyevents #(aget % "keyCode")))

(def modifiers
    (-> keycodes
        (.filter modifier?)
        (rx/map #(modifier-codes %))))

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
    (-> keycodes
        (.filter #(not (modifier? %)))
        (rx/map from-char-code)))
