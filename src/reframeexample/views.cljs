(ns reframeexample.views
  (:require
   [re-frame.core :as re-frame]
   [reframeexample.events :as events]
   [reframeexample.subs :as subs]
   ))

(defn display-user [{:keys [id avatar email first-name :first_name]}]
  [:div.horizontal {:key id}
   [:img.pr-15 {:src avatar}]
   [:div
    [:h2 first-name]
    [:p (str "(" email ")")]]])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        loading (re-frame/subscribe [::subs/loading])
        users (re-frame/subscribe [::subs/users])] ;; subscritions
    [:div
     [:h1
      "Hello from " @name]
     ;; 
     (when @loading "Loading...")
     (map display-user @users)
     ;; 1/ We create the event we want to dispatch
     ;; Events are referenced in events.cljs
     [:button {:on-click #(re-frame/dispatch
                           [::events/update-name "🤣"])} "Update name"]
     [:button {:on-click #(re-frame/dispatch
                           [::events/fetch-users])}"Make API Call"]]))