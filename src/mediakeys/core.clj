(ns mediakeys.core
    (:use [mediakeys.hotkeys :only [register-keys!]])
    (:require [rx.lang.clojure.interop :as rx])
    (:import [rx Observable]))
(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])

(def dose-keys {:foo "control 1" :bar "control 2"})
(def ^Provider current-provider
    (Provider/getCurrentProvider false))

(defn -main [] 
    (let [obs (register-keys! current-provider dose-keys)]
        (.subscribe obs (rx/action [x]
            (println x)))))