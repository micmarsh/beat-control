(ns mediakeys.browser.settings
    (:use [mediakeys.browser.events :only [modifiers characters subscribe]]))


(def print #(.log js/console %))
(subscribe characters print)


(defn apply-modifier! [modifier]
    (if (changing?)
        (let [{:keys [settings which]} @changing
              current-text ((keyword which) @settings)])))


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
