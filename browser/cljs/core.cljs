(ns mediakeys.browser.core
    (:use [mediakeys.browser.view :only [setting-button setting-text]]
           [mediakeys.browser.settings :only [change-setting! settings-changes]]
           [mediakeys.browser.rx :only [into-atom!]])
    (:require [reagent.core :as reagent :refer [atom]]))


(def settings (atom {
        :play "control 1"
        :forward "control 8"
        :back "control 7"
    }))

(into-atom! settings-changes settings)

; (js/setInterval 
;     #(swap! settings assoc
;         (rand-nth (keys @settings)) 
;         (.slice (.toISOString (js/Date.)) 
;             (count "2014-02-23T04:19:")))
; 500)

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