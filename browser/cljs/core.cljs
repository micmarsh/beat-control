(ns mediakeys.browser.core
    (:use [mediakeys.browser.view :only [setting-button setting-text]]
           [mediakeys.browser.settings :only [change-setting! settings-changes]]
           [mediakeys.browser.rx :only [into-atom!]])
    (:require [reagent.core :as reagent :refer [atom]]
              [rx-cljs.observable :as rx]))



(def settings (atom {
        :play "control 1"
        :forward "control 8"
        :back "control 7"
    }))

(def print #(.log js/console %))

(print settings-changes)

(-> settings-changes
    (into-atom! settings)
    (rx/subscribe #()))

(defn main-view []
    [:div#main
        [:h1#title "Yo whatup from Reagent"]
        (for [[action setting] @settings
              action-str [(name action)]]
            ^{:key action}
            [:p
                action-str ": "
                [(setting-text action-str) setting]
                [(setting-button action-str) 
                    {:on-click #(change-setting! @settings action-str)}
                    "change"]])])

(defn ^:export run []
    (reagent/render-component [main-view] (.-body js/document)))  