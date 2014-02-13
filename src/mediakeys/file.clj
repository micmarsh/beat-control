(ns mediakeys.file)

(def HOME (System/getProperty "user.home"))
(def DIR (str HOME "/.mediakeys/"))
(.mkdir (java.io.File. DIR))
(def LOCATION (str DIR "keys"))
(def DEFAULT {
        :play "control space"
        ;TODO look up actual key combinations
        :forward "control right_arrow"
        :back "control left_arrow"
    })
(def DEFAULT_KEYS
    (try (load-file LOCATION)
        (catch java.io.FileNotFoundException e
            DEFAULT)))

; yeah, some typing here would be great
(def save-keys (partial spit LOCATION))