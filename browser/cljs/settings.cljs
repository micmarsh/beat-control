(ns mediakeys.browser.settings
    (:use [mediakeys.browser.events :only [modifiers characters subscribe]]))


(def print #(.log js/console %))
(subscribe characters print)


(defn apply-modifier! [modifier]
    (if (changing?)
        (let [{:keys [settings which]} @changing
              current-text ((keyword which) @settings)])))

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

(subscribe modifiers print)

(def changing (atom {:settings nil :which nil}))
(defn changing? []
    (let [{:keys [settings which]} @changing]
        (and settings which)))

(defn change-setting! [settings button] 
    (.log js/console button)
    (swap! settings assoc (keyword button) "change this to something!")
    (reset! changing {
            :settings settings
            :which button
        }))
