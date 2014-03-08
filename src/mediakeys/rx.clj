(ns mediakeys.rx
    (:require [rx.lang.clojure.interop :as rx])
    (:import [rx Observable]))

(defmacro defn-obs [coll-fn obs-fn]
    `(defn ~coll-fn [^Observable obs# function#]
        (~obs-fn obs#
            (rx/fn* function#))))

(defn-obs mapcat .flatMap)
(defn-obs map .map)
(defn-obs filter .filter)

(defn sub 
    ([^Observable o fn0]
        (sub o fn0 nil nil))
    ([^Observable o fn0 fn1]
        (sub o fn0 fn1 nil))
    ([^Observable o fn0 fn1 fn2]
        (.subscribe o 
            (rx/action* fn0)
            (rx/action* fn1)
            (rx/action* fn2))))

(defn tap [^Observable o fn0]
    (map o (fn [item] (fn0 item) item)))

(defn observable [function]
    (Observable/create 
        (rx/action* function)))

(defn next! [^rx.Subscriber sub thing]
    (.onNext sub thing))
(defn error! [^rx.Subscriber sub thing]
    (.onError sub thing))

(defn from-seq [sequence]
    (observable 
        (fn [sub]
            (doseq [item sequence]
                (next! sub item)))))
