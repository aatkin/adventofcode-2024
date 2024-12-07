^{:nextjournal.clerk/visibility {:code :hide}}
(ns day-1
  {:nextjournal.clerk/toc true}
  (:require [clojure.string :as str]
            [nextjournal.clerk :as clerk]
            [util]))

{::clerk/visibility {:result :hide}}

^{::clerk/visibility {:code :hide}}
(def example-input "3   4
                    4   3
                    2   5
                    1   3
                    3   9
                    3   3")

;; ## Part 1

^{::clerk/visibility {:code :hide :result :show}
  ::clerk/no-cache true}
(util/puzzle-description (slurp "datasets/day-1/part-1.md"))

;; ### Example input

;; To find the total distance between the left list and the right list, add up the distances between all of the pairs you found. In the example above, this is 2 + 1 + 0 + 1 + 2 + 5, a total distance of 11!

(defn parse-all-pairs [input]
  (->> input
       str/split-lines
       (eduction (map util/trim-empty-space)
                 (mapcat #(str/split % #" "))
                 (map parse-long)
                 (partition-all 2))))

^{::clerk/visibility {:result :show}}
(def example-pairs (parse-all-pairs example-input))

(defn distance [left right]
  (->> (interleave (sort left)
                   (sort right))
       (eduction (partition-all 2)
                 (map (partial apply -))
                 (map abs))
       (reduce + 0)))

^{::clerk/visibility {:result :show}}
(distance (map first example-pairs)
          (map second example-pairs))

;; ### Solution

;; Your actual left and right lists contain many location IDs. What is the total distance between your lists?

^{::clerk/no-cache true}
(def puzzle-input (slurp "datasets/day-1/input.txt"))

(def all-pairs (parse-all-pairs puzzle-input))

^{::clerk/visibility {:result :show}}
(distance (map first all-pairs)
          (map second all-pairs))

;; ## Part 2

^{::clerk/visibility {:code :hide :result :show}
  ::clerk/no-cache true}
(util/puzzle-description (slurp "datasets/day-1/part-2.md"))

;; ### Example input

;; This time, you'll need to figure out exactly how often each number from the left list appears in the right list. Calculate a total similarity score by adding up each number in the left list after multiplying it by the number of times that number appears in the right list.

;; So, for these example lists, the similarity score at the end of this process is 31 (9 + 4 + 0 + 0 + 9 + 9) .

(defn similarity-score [left right-freqs]
  (->> left
       (eduction (map (juxt identity right-freqs))
                 (filter (comp some? second))
                 (map (partial apply *)))
       (reduce + 0)))

^{::clerk/visibility {:result :show}}
(similarity-score (map first example-pairs)
                  (frequencies (map second example-pairs)))

;; ### Solution

^{::clerk/visibility {:result :show}}
(similarity-score (map first all-pairs)
                  (frequencies (map second all-pairs)))
