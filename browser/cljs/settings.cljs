(ns mediakeys.browser.settings
    (:use [mediakeys.browser.events :only [modifiers characters subscribe]]))


(defn change-setting! [settings button] 
    (.log js/console button)
    (swap! settings assoc (keyword button) "ASS"))
