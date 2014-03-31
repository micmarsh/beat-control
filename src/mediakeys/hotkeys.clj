(ns mediakeys.hotkeys
    (:use [mediakeys.file :only [DEFAULT_KEYS save-keys!]]
          [mediakeys.utils :only [dochan]]
          [clojure.core.async :only [chan go-loop <! pipe put!]]))

(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])


(defn make-keystroke [keystroke]
    (KeyStroke/getKeyStroke keystroke))

(def not-nil? (comp not nil?))

(defn flatmap< [f channel]
    (let [return (chan)]
        (dochan channel #(pipe (f %) return))
        return))

(def new-provider!
    (let [provider (atom nil)]
        (fn [ ]
            (when @provider
                (doto @provider
                    (.reset)
                    (.stop)))
            (reset! provider 
                (Provider/getCurrentProvider false))
            @provider)))

(def new-keys!
    (let [keys (atom DEFAULT_KEYS)]
        (fn ([] @keys)
            ([key-update]
                (swap! keys #(merge % key-update))))))

(defn register-keys [^Provider provider keys]
    (let [return (chan)]
        (doseq [[action hotkey] keys
                 keystroke [(make-keystroke hotkey)]]
                (.register provider keystroke
                    (proxy [HotKeyListener] []
                        (onHotKey [event]
                            (put! return action)))))
        return))

(defn keypress-channel! [key-change]
    (println "KEYCHANGE" key-change)
    (let [old-keys (new-keys!)]
        (try
            (let [keys (new-keys! key-change)
                  provider (new-provider!)
                  channel (register-keys provider keys)]
                (save-keys! keys)
                channel)
        (catch java.lang.Exception e
            (println "here's the exception, catch it more specifically")
            (.printStackTrace e)
            (new-keys! old-keys)
            (register-keys (new-provider!) old-keys)
            ; TODO grab that^ channel before returning it, 
            ; and pass some error info in
            ))))

(def keypress-events! (partial flatmap< keypress-channel!))

