(ns hello.core
  (:require
   [clojure.string :as string]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(goog-define github-credentials "")

(defn convert-credentials []
  (let [{user :github-user token :github-token}
        (js->clj github-credentials :keywordize-keys true)]
    (if (and user token)
      (js/btoa (str user ":" token))
      (throw
       (js/Error.
        "GitHub credentials must be defined in config.edn")))))

(def search-query
  (r/atom ""))

(def search-results
  (r/atom nil))

(defn clear-search-results []
  (reset! search-results nil))

(defn fetch-search-results [query]
  (let [headers {"Authorization" (str "Basic " (convert-credentials))}
        request (js/Request. (str "https://api.github.com/search/code?q=" query
                                  "+in:file+extension:cljs&sort=indexed")
                             (clj->js {:headers headers}))]
    (-> (js/fetch request)
      (.then #(.json %))
      (.then #(reset! search-results (js->clj (.-items %)))))))

(defn github->rawgit [url]
  (-> url
    (string/replace #"blob/" "")
    (string/replace #"github\.com" "cdn.rawgit.com")))

(defn search-form []
  [:div
   [:input
    {:placeholder "Search"
     :on-change #(reset! search-query (.-value (.-target %)))}]
   [:button
    {:on-click #(do
                  (clear-search-results)
                  (fetch-search-results @search-query))}
    "OK"]])

(defn item [result]
  (let [code (r/atom nil)]
    (r/create-class
     {:component-will-mount
      (fn [_]
        (let [url (github->rawgit (get-in result ["html_url"]))]
          (-> (js/fetch url)
            (.then #(.text %))
            (.then #(reset! code %)))))
      :reagent-render
      (fn [result]
        [:li
         [:h4 (get-in result ["repository" "full_name"])]
         [:p [:a {:href (get-in result ["html_url"])} (get-in result ["path"])]]
         [:div
          [:pre @code]]])})))

(defn main []
  [:div
   [search-form]
   [:div
    [:h2 "Results"]
    [:ol
     (for [result @search-results]
       ^{:key (str "result-" (get-in result ["path"]))}
       [item result])]]])

(def root
  (.getElementById js/document "root"))

(defn init []
  (r/render [main] root))
