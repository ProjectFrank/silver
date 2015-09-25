(ns silver.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [silver.handlers]
            [silver.subs]
            [silver.views :as views]
            [silver.jsonp :as jsonp]
            [cljs.core.async :refer [<!]]))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init [] 
  (re-frame/dispatch-sync [:initialize-db])
  (re-frame/dispatch [:get-more-posts])
  (mount-root))
