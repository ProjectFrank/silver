(ns silver.jsonp
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [put! <! chan]]
            [re-frame.core :as re-frame])
  (:import [goog.net Jsonp]
           [goog Uri]))

(def rchan (chan))

;; create a global jsonp callback
(set! js/foo #(put! rchan (js->clj %)))

(defn get-posts
  [& [after]]
  (let [uri (Uri. (str "https://www.reddit.com/.json?&count=0&limit=100&after=" after))
        req (Jsonp. (Uri. uri) "jsonp")
        out (chan)]
    (.send req nil
           (fn [res] (put! out res))
           (fn [err] (re-frame/dispatch [:get-posts-error err])))
    (go (let [children (-> (<! out)
                           (js->clj)
                           (get-in ["data" "children"]))]
          (map #(do {:title (get-in % ["data" "title"])
                     :id (get-in % ["data" "name"])
                     :thumb (get-in % ["data" "preview" "images" 0 "source" "url"])})
               children)))))
