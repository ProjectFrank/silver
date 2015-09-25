(ns silver.handlers
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core :as re-frame]
            [silver.db :as db]
            [cljs.core.async :refer [<!]]
            [silver.jsonp :as jsonp]))

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/register-handler
 :inc
 (fn [db _]
   (update-in db [:number] inc)))

(re-frame/register-handler
 :get-more-posts
 (fn [db _]
   (if-not (:busy db)
     (do
       (go (let [after (:id (last (:posts db)))
                 posts (<! (jsonp/get-posts after))]
             (re-frame/dispatch [:add-posts posts])))
       (assoc db :busy true))
     (do
       (.log js/console "ignored")
       db))))

(re-frame/register-handler
 :get-posts-error
 (fn [db [_ err]]
   (-> db
       (assoc :busy false)
       (assoc :error err))))

(re-frame/register-handler
 :add-posts
 (fn [db [_ posts]]
   #_(.log js/console "add-posts: " (clj->js posts))
   (let [new-posts (into [] (distinct (into (:posts db) posts)))]
     (-> db
         (assoc :posts new-posts)
         (assoc :busy false)))))

(re-frame/register-handler
 :remove-by-id
 (fn [db [_ id]]
   (let [old-posts (:posts db)
         new-posts (into [] (remove #(= id (:id %)) old-posts))]
     (assoc db :posts new-posts))))
