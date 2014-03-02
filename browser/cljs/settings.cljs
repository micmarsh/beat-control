(ns mediakeys.browser.settings
    (:use [mediakeys.browser.events :only [modifiers characters subscribe]]
          [mediakeys.browser.rx :only [from-async multi-sub]])
    (:require [rx-cljs.observable :as rx]))



(defn append-text [settings which text]
    (let [old-text (settings which)
          new-text (str old-text " " text)]
        (assoc settings which new-text)))

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

(-> modifiers
    from-async
    (rx/subscribe #(.log js/console %)))
; (subscribe modifiers (with-append))

(-> characters
    from-async
    (rx/subscribe #()))

(def changing-sub (atom nil))

(def incoming-changes
    (rx/create (fn [sub] 
        (reset! changing-sub sub))))

(def settings-changes
    (rx/map incoming-changes
        (fn [{:keys [settings which 
                    changing new-text]}]
            (if changing
                (assoc settings which new-text)
                settings))))

(defn change-setting! [settings button] 
    (let [action (keyword button)]
        (rx/on-next @changing-sub 
            {:settings settings
             :which action
             :new-text "Enter a new hotkey combintation"
             :changing true})))
