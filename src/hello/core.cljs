(ns hello.core
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]))

(def root
  (.getElementById js/document "root"))

(defn main []
  [:div
   [:h1 "Hello World"]])

(defn init []
  (r/render [main] root))
