(ns hello.views
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]))

(defn search-form []
  (let [search-query (rf/subscribe [:form :search])]
    [:div
     [:input
      {:placeholder "Search"
       :default-value (or @search-query "")
       :on-blur #(rf/dispatch [:update-form [:form :search] (.-value (.-target %))])}]
     [:button
      {:on-click #(rf/dispatch [:submit-search @search-query])}
      "OK"]]))

(defn item [{:strs [html_url] :as result}]
  (let [code (rf/subscribe [:data :code html_url])]
    (r/create-class
     {:component-will-mount
      (fn [_]
        (rf/dispatch [:fetch-code html_url]))
      :reagent-render
      (fn [{:strs [html_url path repository] :as result}]
        [:li
         [:h4 (get-in repository ["full_name"])]
         [:p [:a {:href html_url} path]]
         [:div
          [:pre @code]]])})))

(defn main []
  (let [search-results (rf/subscribe [:data :results])]
    [:div
     [search-form]
     [:div
      [:h2 "Results"]
      [:ol
       (for [result @search-results]
         ^{:key (str "result-" (get-in result ["path"]))}
         [item result])]]]))
