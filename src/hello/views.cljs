(ns hello.views
  (:require
   cljsjs.highlight
   cljsjs.highlight.langs.clojure
   [clojure.string :as string]
   [hello.styles :as styles]
   [hello.util :as util]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(def logo-url
  "https://github.com/Day8/re-frame/raw/master/images/logo/re-frame_128w.png?raw=true")

(defn search-form []
  (let [search-query (rf/subscribe [:form :search])]
    [:div {:style styles/form-wrapper}
     [:div
      [:img
       {:style styles/logo-image
        :src logo-url}]]
     [:div
      [:form
       {:style styles/form
        :on-submit #(do (.preventDefault %)
                        (rf/dispatch [:submit-search @search-query]))}
       [:div {:style styles/input-wrapper}
        [:input.fancy
         {:style styles/input
          :placeholder "Search"
          :default-value (or @search-query "")
          :on-blur #(rf/dispatch [:update-form [:form :search] (.-value (.-target %))])}]]
       [:div {:style styles/submit-wrapper}
        [:button
         {:style styles/submit-button
          :type "submit"}
         "OK"]]]]]))

(defn highlight-code! [this]
  (.highlightBlock js/hljs (.querySelector (r/dom-node this) "code")))

(defn source-code [{:keys [code]}]
  (r/create-class
   {:component-did-update highlight-code!
    :component-did-mount  highlight-code!
    :reagent-render
    (fn [{:keys [code]}]
      [:div {:style styles/source-code-wrapper}
       [:pre {:style styles/source-code-inner-wrapper}
        [:code code]]])}))

(defn get-avatar [result]
  (get-in result ["repository" "owner" "avatar_url"]))

(defn user [{:keys [user result]}]
  [:div {:style styles/user-wrapper}
   [:a
    {:style styles/user-link
     :href (str "https://github.com/" user)
     :target "_blank"}
    [:img
     {:style styles/user-image
      :src (get-avatar result)}]
    [:div {:style styles/user-name}
     user]]])

(defn repo-and-file [{:keys [html_url path name repo user]}]
  [:div {:style styles/repo-wrapper}
   [:div
    [:a
     {:style styles/file-link
      :href html_url :title path}
     name]]
   [:div
    [:a
     {:style styles/repo-link
      :href (str "https://github.com/" user "/" repo)}
     repo]]])

(defn item [{:strs [html_url] :as result}]
  (let [code (rf/subscribe [:data :code html_url])]
    (r/create-class
     {:component-will-mount
      (fn [_]
        (rf/dispatch [:fetch-code html_url]))
      :reagent-render
      (fn [{:strs [html_url path name repository] :as result}]
        (let [[username repo] (string/split (get-in repository ["full_name"]) #"/")]
          [:div.result {:style (styles/result-wrapper @code)}
           [user
            {:result result
             :user username}]
           [repo-and-file
            {:repo repo
             :user username
             :path path
             :name name
             :html_url html_url}]
           [source-code
            {:code @code}]]))})))

(defn results []
  (let [search-results (rf/subscribe [:data :results])]
    [:div
     (for [result @search-results]
       ^{:key (str "result-" (get-in result ["path"]))}
       [item result])]))

(defn main []
  [:div {:style styles/wrapper}
   [search-form]
   [results]])
