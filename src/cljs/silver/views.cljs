(ns silver.views
    (:require [re-frame.core :as re-frame]))

(defn le-reddit-army []
  (fn [posts]
    [:div
     (map (fn [{:keys [id title thumb]}]
            [:div {:class "post"
                   :key (str "reddit-" id)}
             [:div {:style {:background-image (str "url(" thumb ")")}
                    :class "thumb"}]
             [:div {:class "text-content"}
              [:div {:class "title"} title]
              [:div {:on-click #(re-frame/dispatch [:remove-by-id id])
                     :class "hide-button"} " Hide"]]])
          posts)]))

(defn load-more-button []
  (fn []
    [:button {:on-click #(re-frame/dispatch [:get-more-posts])} "Load more posts"]))

(defn main-panel []
  (let [name (re-frame/subscribe [:name])
        number (re-frame/subscribe [:number])
        posts (re-frame/subscribe [:posts])
        error (re-frame/subscribe [:error])]
    (fn []
      [:div
       [:div "Hello from " @name]
       [:div {:on-click #(re-frame/dispatch [:inc])} "Number of posts: " (count @posts)]
       (when @error
         [:div (str "Oops! There was an error: " (clj->js error))])
       [load-more-button]
       [le-reddit-army @posts]
       [load-more-button]])))
