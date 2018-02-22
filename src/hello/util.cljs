(ns hello.util
  (:require
   [clojure.string :as string]))

(defn convert-to-basic-auth [creds]
  (let [{user :github-user token :github-token} creds]
    (if (and user token)
      (js/btoa (str user ":" token))
      (throw
       (js/Error.
        "GitHub credentials must be defined in config.edn")))))

(defn github->rawgit [url]
  (-> url
    (string/replace #"blob/" "")
    (string/replace #"github\.com" "cdn.rawgit.com")))

(defn sanitize-url [url]
  (string/replace url #"[.:/]+" "_"))

