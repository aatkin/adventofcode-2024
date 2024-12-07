^{:nextjournal.clerk/visibility {:code :hide}}
(ns day-2
  {:nextjournal.clerk/toc true}
  (:require [clojure.string :as str]
            [medley.core :refer [find-first]]
            [nextjournal.clerk :as clerk]
            [util]))

{::clerk/visibility {:result :hide}}

;; ## Part 1

^{::clerk/visibility {:code :hide :result :show}
  ::clerk/no-cache true}
(util/puzzle-description (slurp "datasets/day-2/part-1.md"))

;; So, a report only counts as safe if both of the following are true:
;; - The levels are either all increasing or all decreasing.
;; - Any two adjacent levels differ by at least one and at most three.

;; ### Example input

(def example-input "7 6 4 2 1
                    1 2 7 8 9
                    9 7 6 2 1
                    1 3 2 4 5
                    8 6 4 4 1
                    1 3 6 7 9")

;; So, in this example, 2 reports are safe.

(defn parse-levels [input]
  (for [line (str/split-lines input)]
    (-> line
        str/trim
        (str/split #" ")
        (->> (map parse-long)))))

(defn adjacent-diff [levels]
  (->> levels
       (partition 2 1)
       (map #(apply - %))))

(def illegal-diff? (some-fn zero? #(< 3 (abs %))))
(def safe? (comp (partial not-any? illegal-diff?)
                 adjacent-diff))

(def increasing-or-decreasing?
  (some-fn (partial apply <)
           (partial apply >)))

(defn safe-reports [levels]
  (->> levels
       (eduction (filter increasing-or-decreasing?))
       (filterv safe?)))

^{::clerk/visibility {:result :show}}
(def safe (safe-reports (parse-levels example-input)))

^{::clerk/visibility {:result :show}}
(count safe)

;; ### Solution

;; Analyze the unusual data from the engineers. How many reports are safe?

^{::clerk/no-cache true}
(def puzzle-input (slurp "datasets/day-2/input.txt"))

^{::clerk/visibility {:result :show}}
(count (safe-reports (parse-levels puzzle-input)))

;; ## Part 2

^{::clerk/visibility {:code :hide :result :show}
  ::clerk/no-cache true}
(util/puzzle-description (slurp "datasets/day-2/part-2.md"))

;; ### Example input

(defn drop-nth [n coll]
  (keep-indexed #(when-not (= %1 n) %2) coll))

(defn generate-variants [levels]
  (let [n (count levels)]
    (loop [variants [levels]
           curr 0]
      (if (= curr n)
        variants
        (recur (conj variants (drop-nth curr levels))
               (inc curr))))))

(defn safe-reports-v2 [levels]
  (let [safe (->> levels
                  (filter increasing-or-decreasing?)
                  (filter safe?))
        unsafe (concat (remove increasing-or-decreasing? levels)
                       (->> levels
                            (filter increasing-or-decreasing?)
                            (remove safe?)))]
    (concat safe
            (->> unsafe
                 (map generate-variants)
                 (keep (partial find-first (every-pred increasing-or-decreasing?
                                                       (comp true? safe?))))))))

^{::clerk/visibility {:result :show}}
(def safe-v2 (safe-reports-v2 (parse-levels example-input)))

^{::clerk/visibility {:result :show}}
(count safe-v2)

;; Update your analysis by handling situations where the Problem Dampener can remove a single level from unsafe reports. How many reports are now safe?

;; ### Solution

^{::clerk/visibility {:result :show}}
(count (safe-reports-v2 (parse-levels puzzle-input)))
