(ns mediakeys.browser.settings
    (:use [cljs.core.async :only [chan <! map< filter< put!]])
    (:use-macros [cljs.core.async.macros :only [go]]))

(def keypresses (chan))

(def modifier-codes {
        16 "shift"
        17 "control"
        18 "alt"
    })

(.addEventListener js/document "keydown"
    (fn [event]
        (put! keypresses event)))

(def modifiers
    (->> keypresses
        (map<  #(.-keyCode %))
        (filter< #(contains? modifier-codes %))
        (map<  #(modifier-codes % ))))
(go 
    (while true
        (let [thing (<! modifiers)]
            (.log js/console thing))))

(defn change-setting! [settings button] 
    (.log js/console button)
    (swap! settings assoc (keyword button) "ASS"))
