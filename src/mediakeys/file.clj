(ns mediakeys.file)

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
    (try (load-file LOCATION)
        (catch java.io.FileNotFoundException e
            DEFAULT)))

; yeah, some typing here would be great
(def save-keys! (partial spit LOCATION))