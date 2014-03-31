(ns mediakeys.hotkeys
    (:use [mediakeys.file :only [DEFAULT_KEYS save-keys!]]
           clojure.core.async))

(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])

(def not-nil? (comp not nil?))

(defn flatmap< [f channel]
    (let [return (chan)]
        (go-loop []
            (let [value (<! channel)]
                (when not-nil? value
                    (go-loop [unflattened (f value)]
                        (let [inner-value (<! unflattened)]
                            (when (not-nil? inner-value)
                                (>! return inner-value)
                                (recur unflattened))))
                    (recur))))
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
    (let [keys (atom nil)]
        (fn [key-update]
            (swap! keys #(merge key-update))
            @keys)))

(defn register-keys [^Provider provider keys]
    (let [return (chan)]
        (doseq [[action hotkey] keys
                 keystroke [(make-keystroke hotkey)]]
                (.register provider keystroke
                    (proxy [HotKeyListener] []
                        (onHotKey [event]
                            (put! return action)))))
        return))

(defn keypress-channel! [key-changes]
    (let [old-keys (new-keys! { })]
        (try
            (let [keys (new-keys! key-changes)
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
            ; and pass some error info into it
            ))))

(def keypress-events! (partial flatmap< keypress-channel!))

