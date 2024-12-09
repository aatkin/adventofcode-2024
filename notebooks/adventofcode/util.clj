(ns adventofcode.util
  (:require [clojure.string :as str]))

(defn slurp-if-exists [path]
  (try
    (slurp path)
    (catch Exception _
      "")))

(defn trim-empty-space [s]
  (-> s
      str/trim
      (str/replace #"\s{2,}" " ")))

(defn not-blank [x]
  (when (and (string? x)
             (not (str/blank? x)))
    x))
