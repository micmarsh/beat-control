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

(def current-keys (chan))

(def DEFAULT_KEYS
    (try (let [keys (load-file LOCATION)]
            (put! current-keys keys)
            keys)
        (catch java.io.FileNotFoundException e
            DEFAULT)))

(defn save-keys! [keys]
    (put! current-keys keys)
    (spit LOCATION keys))
