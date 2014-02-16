(ns mediakeys.core
    (:use [mediakeys.hotkeys :only [keypress-events!]])
    (:require [rx.lang.clojure.interop :as rx])
    (:import [rx Observable]))
(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])

(def dose-keys [{:play "control 1" :forward "control 2"}
                {:back "control 4" :play "control 3"}])

(defn from-seq [sequence]
    (Observable/create 
        (rx/action [^rx.Subscriber s]
            (doseq [item sequence]
                (.onNext s item)))))

(def ^Provider current-provider
    (Provider/getCurrentProvider false))

(defn -main [] 
    (let [changes (from-seq dose-keys)
          presses (keypress-events! changes)]
        (.subscribe presses 
            (rx/action [x]
                (println x)))))