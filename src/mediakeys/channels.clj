(ns mediakeys.channels
    (:use [clojure.core.async :only [chan]]))

(def update-keys (chan))

(def keymaster-errors (chan))