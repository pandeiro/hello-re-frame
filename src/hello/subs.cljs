(ns hello.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :form
 (fn [db [_ & path]]
   (get-in db (concat [:form] path))))

(rf/reg-sub
 :data
 (fn [db [_ & path]]
   (get-in db (concat [:data] path))))
