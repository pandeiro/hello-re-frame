(merge-env!
 :resource-paths #{"resources"}
 :source-paths #{"src"}
 :dependencies
 '[
   [adzerk/boot-cljs "2.1.4"]
   [adzerk/boot-reload "0.5.2"]
   [binaryage/devtools "0.9.9"]
   [org.clojure/clojure "1.9.0"]
   [org.clojure/clojurescript "1.9.946"]
   [pandeiro/boot-http "0.8.3"]
   [re-frame "0.10.5"]
   ])

(require
 '[adzerk.boot-cljs :refer [cljs]]
 '[adzerk.boot-reload :refer [reload]]
 '[pandeiro.boot-http :refer [serve]])

(deftask dev []
  (comp
   (serve :port 8888)
   (watch)
   (reload :on-jsload 'hello.core/init)
   (cljs :compiler-options
         {:closure-defines {'hello.core/github-credentials
                            (-> "config.edn" slurp read-string)}})))
