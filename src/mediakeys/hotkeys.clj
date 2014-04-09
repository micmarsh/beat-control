(ns mediakeys.hotkeys
    (:use [mediakeys.file :only [DEFAULT_KEYS save-keys!]]
          [mediakeys.utils :only [dochan defcurried]]
          [mediakeys.channels :only [keymaster-errors]]
          [clojure.core.async :only [chan go-loop <! pipe put!]]))

(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])


(defn make-keystroke [keystroke]
    (KeyStroke/getKeyStroke keystroke))

(def not-nil? (comp not nil?))

(defcurried flatmap< [f channel]
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

(defn with-error! [action hotkey old-keys]
    (put! keymaster-errors {:action action :hotkey hotkey})
    (-> action old-keys make-keystroke))

(defn register-keys [^Provider provider keys old-keys]
    (let [return (chan)]
        (doseq [[action hotkey] keys
                 keystroke [(or (make-keystroke hotkey)
                                (with-error! action hotkey old-keys))]]
                (.register provider keystroke
                    (proxy [HotKeyListener] []
                        (onHotKey [event]
                            (put! return action)))))
        return))

(defn keypress-channel! [key-change]
    (let [old-keys (new-keys!)]
            (let [keys (new-keys! key-change)
                  provider (new-provider!)
                  channel (register-keys provider keys old-keys)]
                ; shouldn't get to save keys, BUT IT IS
                (save-keys! keys)
                channel)))

(defcurried seed-channel! [seed channel]
    (println "yo seedding channel")
    (put! channel seed)
    channel)

(defn keypress-events! [key-changes]
    (let [result (flatmap< keypress-channel! key-changes)]
        (seed-channel! "{}" key-changes)
        result))
