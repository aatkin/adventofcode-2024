(ns user
  (:require [clojure.string]
            [clojure.tools.namespace.repl :as repl]
            [nextjournal.clerk :as clerk]))

(comment
  (repl/clear)
  (repl/refresh-all)

  (clerk/serve! {:browse? true
                 :index "notebooks/adventofcode/index.clj"
                 :watch-paths ["notebooks/adventofcode"]
                 :paths ["notebooks/adventofcode/*"]
                 :show-filter-fn #(clojure.string/starts-with? % "notebooks/adventofcode/day")})
  (clerk/clear-cache!))
