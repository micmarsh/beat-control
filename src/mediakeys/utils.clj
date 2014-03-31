(ns mediakeys.utils
    (:use [clojure.core.async :only [go-loop <!]]))

;courtesy of https://gist.github.com/sunilnandihalli/745654
(defmacro defcurried [name args & body]
  {:pre [(not-any? #{'&} args)]}
  (if (empty? args)
    `(defn ~name ~args ~@body)
    (let [rec-funcs (reduce (fn [l v]
                              `(letfn [(helper#
                                         ([] helper#)
                                         ([x#] (let [~v x#] ~l))
                                         ([x# & rest#] (let [~v x#]
                                                         (apply (helper# x#) rest#))))]
                                 helper#))
                            `(do ~@body) (reverse args))]
      `(defn ~name [& args#]
         (let [helper# ~rec-funcs]
           (apply helper# args#))))))

(defn dochan [channel function!]
    (go-loop []
        (let [value (<! channel)]
            (when (-> value nil? not)
                (function! value)
                (recur)))))