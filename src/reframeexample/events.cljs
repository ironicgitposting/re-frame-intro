(ns reframeexample.events
  (:require
   [re-frame.core :as re-frame]
   [reframeexample.db :as db]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

;; 2/ We create the event handler for the event we dispatched in view::main-panel
(re-frame/reg-event-db
 ;; we dispatched ::events/update-name,
 ;; so we create an event handler names update-name
 ::update-name
 (fn [db  [_event_name val]]
   (assoc db :name val)))

;; Fetch data from API
(re-frame/reg-event-fx                             ;; note the trailing -fx
 ::fetch-users                      ;; usage:  (dispatch [:handler-with-http])
 (fn [{:keys [db]} _]                    ;; the first param will be "world"
   {:db   (assoc db :loading true)   ;; causes the twirly-waiting-dialog to show??
    :http-xhrio {:method          :get
                 :uri             "https://reqres.in/api/users?page=2"
                 :timeout         8000                                           ;; optional see API docs
                 :response-format (ajax/json-response-format {:keywords? true})  ;; IMPORTANT!: You must provide this.
                 :on-success      [::fetch-users-success]
                 :on-failure      [:bad-http-result]}}))

(re-frame/reg-event-db
 ::fetch-users-success
 (fn [db [_event-name {:keys [data]}]]   
   (-> db
       (assoc :loading false)
       (assoc :users data)))) ;; db is the first argument of assoc