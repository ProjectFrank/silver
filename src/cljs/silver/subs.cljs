(ns silver.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/register-sub
 :name
 (fn [db]
   (reaction (:name @db))))

(re-frame/register-sub
 :number
 (fn [db]
   (reaction (:number @db))))

(re-frame/register-sub
 :posts
 (fn [db]
   (reaction (do
               (.log js/console (clj->js (:posts @db)))
               (:posts @db)))))

(re-frame/register-sub
 :error
 (fn [db]
   (reaction (:error @db))))
