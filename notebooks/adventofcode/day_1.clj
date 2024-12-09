^{:nextjournal.clerk/visibility {:code :hide}}
(ns adventofcode.day-1
  {:nextjournal.clerk/toc true}
  (:require [clojure.string :as str]
            [nextjournal.clerk :as clerk]
            [adventofcode.util :as util]))

{::clerk/visibility {:result :hide}}

;; ## Part 1

^{::clerk/no-cache true}
(def example-input (slurp "datasets/day-1/example-input.txt"))

^{::clerk/no-cache true}
(def puzzle-input (util/slurp-if-exists "datasets/day-1/input.txt"))

;; ### Solution

(defn parse-all-pairs [input]
  (->> input
       str/split-lines
       (eduction (map util/trim-empty-space)
                 (mapcat #(str/split % #" "))
                 (map parse-long))
       (into [] (partition-all 2))))

(defn distance [left right]
  (->> (interleave (sort left)
                   (sort right))
       (eduction (partition-all 2)
                 (map (partial apply -))
                 (map abs))
       (reduce + 0)))

;; ### Example input
{::clerk/visibility {:result :show}}

(def example-pairs (parse-all-pairs example-input))

(distance (map first example-pairs)
          (map second example-pairs))

;; ### Input

(def all-pairs (some-> (util/not-blank puzzle-input)
                       parse-all-pairs))

(when all-pairs
  (distance (map first all-pairs)
            (map second all-pairs)))

{::clerk/visibility {:result :hide}}
;; ## Part 2

;; ### Solution

(defn similarity-score [left right-freqs]
  (->> left
       (eduction (map (juxt identity right-freqs))
                 (filter (comp some? second))
                 (map (partial apply *)))
       (reduce + 0)))

;; ### Example input
{::clerk/visibility {:result :show}}

(similarity-score (map first example-pairs)
                  (frequencies (map second example-pairs)))

;; ### Input

(when all-pairs
  (similarity-score (map first all-pairs)
                    (frequencies (map second all-pairs))))

{::clerk/visibility {:result :hide}}
