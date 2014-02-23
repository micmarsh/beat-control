(ns mediakeys.browser.view)

(defn- make-tag [{:keys [tag class id]}]
    (keyword (str tag 
        (if id  (str "#" id) "") 
        (if class (str "." class) ""))))

(defn setting-text [action]
    (make-tag {
            :tag "span"
            :id action
            :class "setting"
        }))
(defn setting-button [action]
    (make-tag {
        :tag "button"
        :id action
        :class "change"
        }))
