(ns mediakeys.file
    (:use [clojure.core.async :only [chan put!]]))

(def HOME (System/getProperty "user.home"))
(def DIR (str HOME "/.mediakeys/"))
(.mkdir (java.io.File. DIR))
(def LOCATION (str DIR "keys"))
(def DEFAULT {
        :play "control 1"
        :forward "control 8"
        :back "control 7"
    })
(def DEFAULT_KEYS
    (try (let [keys (load-file LOCATION)]
            (put! save-keys keys)
            keys)
        (catch java.io.FileNotFoundException e
            DEFAULT)))

(def saved-keys (chan))
(defn save-keys! [keys]
    (put! saved-keys keys)
    (spit LOCATION keys))
