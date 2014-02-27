(ns mediakeys.browser.settings
    (:use [mediakeys.browser.events :only [modifiers characters subscribe]]))


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


(def changing (atom nil))
(def changing? #(-> @changing nil? not))

(defn append-text [settings which text]
    (let [old-text (settings which)
          new-text (str old-text " " text)]
        (assoc settings which new-text)))

(defn with-append [callback]
    (fn [keystroke]
        (when (changing?)
            (let [{:keys [which settings]} @changing
                  new-text (swap! settings append-text which keystroke)]
                (when (not (nil? callback))
                    (callback which new-text))))))


(subscribe modifiers (with-append))

(subscribe characters
    (with-append (fn [which text]
            ;TODO send to server! 
            (reset! changing nil))))

(defn change-setting! [settings button] 
    (let [action (keyword button)]
        (.log js/console button)
        (swap! settings assoc action "enter a new key combination")
        (reset! changing {
                :settings settings
                :which action
            })))
