(ns mediakeys.browser.settings
    (:use [mediakeys.browser.events :only [modifiers characters]]
          [mediakeys.browser.rx :only [multi-sub]])
    (:require [rx-cljs.observable :as rx]))


(def clicks-sub (atom nil))
(def incoming-clicks
    (rx/create #(reset! clicks-sub %)))

(def changing
    (-> modifiers
        (.merge incoming-clicks)
        (rx/map #(identity 1))
        (.merge characters)
        (multi-sub #(.log js/console %))
        (rx/map #(number? %))))

(defn only-when [predicates observable]
    (-> observable
        (rx/zip predicates
            (fn [item bool]
             {:item item :bool bool}))
        (.filter #(% :bool))
        (rx/map #(% :item))))

(def valid-modifiers (only-when changing modifiers))
(def valid-characters (only-when changing characters))

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
