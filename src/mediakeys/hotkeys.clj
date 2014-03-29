(ns mediakeys.hotkeys
    (:use [mediakeys.file :only [DEFAULT_KEYS save-keys!]]
           clojure.core.async))

(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])

(defn make-keystroke [keystroke]
    (KeyStroke/getKeyStroke keystroke))

(def current-keys (atom DEFAULT_KEYS))

(defn register-keys! [^Provider provider keys]
    (observable 
        (fn [s]
            (doseq [[action hotkey] keys
                    keystroke [(make-keystroke hotkey)]]
                (.register provider keystroke
                    (proxy [HotKeyListener] []
                        (onHotKey [event]
                            (next! s action))))))))

(def provider (atom (Provider/getCurrentProvider false)))

(defn key-config! [key-changes]
    (map key-changes
        (fn [key-update]
            (swap! current-keys #(merge % key-update))
            (save-keys! @current-keys)
            @current-keys)))

(defn keypress-events! [key-changes]
    (-> key-changes
        key-config!
        (mapcat (fn [all-keys]
            (let [new-provider (Provider/getCurrentProvider false)
                  result (register-keys! new-provider all-keys)]
                  (doto @provider
                      (.reset)
                      (.stop))
                  (reset! provider new-provider)
                  result)))))

(def new-provider
    (let [provider (atom nil)]
        (fn [_]
            (when @provider
                (doto @provider
                    (.reset)
                    (.stop)))
            (reset! provider 
                (Provider/getCurrentProvider false))
            @provider)))

(defn register-keys [^Provider provider keys]
    (let [return (chan)]
        (doseq [[action hotkey] item
                 keystroke [(make-keystroke hotkey)]]
                (.register provider keystroke
                    (proxy [HotKeyListener] []
                        (onHotKey [event]
                            (put! return action)))))
        return))