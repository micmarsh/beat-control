(ns mediakeys.browser.settings)


(defn change-setting! [settings button] 
    (.log js/console button)
    (swap! settings assoc (keyword button) "ASS"))
