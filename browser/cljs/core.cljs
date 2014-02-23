(ns mediakeys.browser.core
    (:use [mediakeys.browser.view :only [setting-button setting-text]]
           [mediakeys.browser.settings :only [change-setting!]])
    (:require [reagent.core :as reagent :refer [atom]]))


(def settings (atom {
        :play "control 1"
        :forward "control 8"
        :back "control 7"
    }))

(js/setInterval 
    (fn []
        (swap! settings assoc
            (rand-nth (keys @settings)) 
            (.toISOString (js/Date.)))) 
500)

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
                    {:on-click #(change-setting! settings action-str)}
                    "change"]])])

(defn ^:export run []
  (reagent/render-component [main-view] (.-body js/document)))  