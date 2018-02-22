(ns hello.styles)

;;
;; Colors
;;
(def colors
  {:primary "#6d93e3"
   :secondary "#8bd644"
   :primary-text "#2b2b2b"
   :secondary-text "#4d4d4d"
   :primary-background "#ffffff"
   :secondary-background "#f8f8f8"})

;;
;; Components
;;
(def wrapper
  {:display "flex"
   :flex-direction "column"
   :align-items "center"
   :width "100%"})

;;
;; Form
;;
(def form-wrapper
  {:display "flex"
   :flex-direction "row"})

(def logo-image
  {:padding-top "18px"})

(def form
  {:display "flex"
   :flex-direction "row"
   :align-items "center"})

(def input-wrapper
  {:padding "12px"})

(def input
  {:font-size "20px"
   :padding "12px 24px"
   :border-radius "4px"
   :border "1px solid #eee"})

(def submit-wrapper
  {:padding "12px 0"
   :display "inline-block"})

(def submit-button
  {:border-radius "4px"
   :border "none"
   :font-size "18px"
   :background-color (:primary colors)
   :padding "12px 24px"
   :color "#fff"})

;;
;; Results
;;
(def source-code-wrapper
  {:height "192px"
   :overflow "scroll"
   :flex 1
   :padding "0 12px"
   :position "relative"
   :background-color (:secondary-background colors)})

(def source-code-inner-wrapper
  {:margin 0
   :max-width "720px"
   :overflow-x "auto"})

(defn result-wrapper [code]
  {:display "flex"
   :flex-direction "row"
   :background-color (:secondary-background colors)
   :padding "12px"
   :margin "12px 0"
   :opacity (if (not-empty code) 1 0)})

(def user-wrapper
  {:padding "0.5em"
   :opacity 0.6
   :width "96px"})

(def user-link
  {:text-decoration "none"
   :color (:primary-text colors)})

(def user-image
  {:height "72px"
   :width "72px"
   :border-radius "4px"})

(def user-name
  {:font-size "0.8em"})

(def repo-wrapper
  {:padding "0.5em"
   :width "128px"})

(def repo-link
  {:color (:secondary-text colors)
   :text-decoration "none"})

(def file-link
  {:color (:primary colors)
   :text-decoration "none"})
