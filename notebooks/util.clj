(ns util
  (:require [clojure.string :as str]
            [nextjournal.clerk :as clerk]
            [nextjournal.markdown :as md]
            [nextjournal.markdown.transform :as md.transform]))

(defn trim-empty-space [s]
  (-> s
      str/trim
      (str/replace #"\s{2,}" " ")))

(defn puzzle-description [markdown]
  (clerk/html
   [:details [:summary "Toggle puzzle description"]
    (md/->hiccup markdown)]))

#_(defn puzzle-description [& paragraphs]
  (clerk/html (apply conj
                     [:details [:summary "Toggle puzzle description"]]
                     (for [p paragraphs]
                       (if (string? p)
                         [:p p]
                         p)))))

(defn puzzle-example [rows]
  (->> rows
       (map trim-empty-space)
       (interpose [:br])
       (into [:pre])))
