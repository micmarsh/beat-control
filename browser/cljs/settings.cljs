(ns mediakeys.browser.settings
    (:use [mediakeys.browser.events :only [modifiers characters subscribe]]
          [mediakeys.browser.rx :only [from-async multi-sub]])
    (:require [rx-cljs.observable :as rx]))





; (def print #(.log js/console %))
; (subscribe modifiers print)

;TODO! how to set what u need to set
; when a modifier comes in, check to see if u should set something
; if so, check to see if the text is ONLY MODIFIERS OR NOTHING if it is,
; then u can append the modifier to the string and pop it back into settings
;
; when a "character" (not actually characters, necesarily) comes in
; check to see if there's one or more modifiers as the text. if YES
; append this character, send shit 2 the server, (also want a socket interface to make 
; setting accurate anyhow), then reset changing and any other UI stuff to nil

; both of those^ scenarios are if changing has been set by change-setting!

; (subscribe modifiers (with-append))

; (-> characters
;     from-async
;     (rx/subscribe #()))

(def clicks-sub (atom nil))
(def incoming-clicks
    (rx/create #(reset! clicks-sub %)))

(def changing
    (-> incoming-clicks
        (.merge (from-async characters))
        (rx/map #(map? %))
        (multi-sub #(.log js/console %))
))

(defn only-when [predicates observable]
    (-> observable
        (rx/zip predicates
            (fn [item bool]
             {:item item :bool bool}))
        (.filter #(% :bool))
        (multi-sub #(.log js/console %))
        (rx/map #(% :item))
))

(def valid-modifiers (only-when changing 
                        (from-async modifiers)))
(def valid-characters (only-when changing
                        (from-async characters)))

(defn append-text [{:keys [settings button]} text]
    (let [old-text (settings button)
          new-text (str old-text " " text)]
        (assoc settings button new-text)))

(def settings-changes 
    (rx/zip incoming-clicks valid-modifiers append-text))

(defn change-setting! [settings button] 
    (let [action (keyword button)]
        (rx/on-next @clicks-sub
            {:settings settings
             :button action})))
