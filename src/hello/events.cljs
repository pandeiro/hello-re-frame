(ns hello.events
  (:require
   [hello.util :as util]
   [re-frame.core :as rf]))

(def interceptors
  [rf/debug])

(rf/reg-event-db
 :init
 interceptors
 (fn [db [_ state]]
   (merge db state)))

(rf/reg-event-db
 :update-form
 interceptors
 (fn [db [_ path value]]
   (assoc-in db path value)))

(rf/reg-event-fx
 :submit-search
 interceptors
 (fn [{:keys [db]} [_ query]]
   {:db db
    :dispatch [:fetch-search query]}))

(rf/reg-event-fx
 :fetch-search
 interceptors
 (fn [{:keys [db]} [_ query]]
   (let [creds (get-in db [:credentials])
         url   (str "https://api.github.com/search/code?q=re-frame.core+" query
                    "+in:file+extension:cljs&sort=indexed")
         auth  (str "Basic " (util/convert-to-basic-auth creds))]
     {:db db
      :fetch [url {:headers {"Authorization" auth}
                   :success [:search-success]
                   :metdata {:query query}}]})))

(rf/reg-event-db
 :search-success
 interceptors
 (fn [db [_ {:keys [body]}]]
   (assoc-in db [:data :results] (js->clj (.-items body)))))

(rf/reg-event-fx
 :fetch-code
 interceptors
 (fn [{:keys [db]} [_ url]]
   (let [rawgit-url (util/github->rawgit url)]
     {:db db
      :fetch [rawgit-url {:datatype :text
                          :success [:code-success]
                          :metadata {:url url}}]})))

(rf/reg-event-db
 :code-success
 interceptors
 (fn [db [_ {:keys [body metadata]}]]
   (assoc-in db [:data :code (:url metadata)] body)))

;;
;; Effects
;;
(rf/reg-fx
 :fetch
 (fn [[url {:keys [success error headers datatype metadata]
            :or {datatype :json}}]]
   (let [request (if headers
                   (js/Request. url (clj->js {:headers headers}))
                   (js/Request. url))]
     (-> (js/fetch request)
       (.then #(if (= "text" (name datatype))
                 (.text %)
                 (.json %)))
       (.then #(doseq [ev success]
                (rf/dispatch [ev {:body % :metadata metadata}])))
       (.catch #(doseq [ev error]
                 (rf/dispatch [ev {:error % :metadata metadata}])))))))
