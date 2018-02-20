(ns hello.core
  (:require
   [hello.events]
   [hello.subs]
   [hello.views :as views]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(goog-define github-credentials "")

(def root
  (.getElementById js/document "root"))

(defn init []
  (rf/dispatch-sync [:init {:credentials (js->clj github-credentials :keywordize-keys true)}])
  (r/render [views/main] root))
