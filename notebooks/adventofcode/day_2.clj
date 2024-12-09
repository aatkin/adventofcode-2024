^{:nextjournal.clerk/visibility {:code :hide}}
(ns adventofcode.day-2
  {:nextjournal.clerk/toc true}
  (:require [clojure.string :as str]
            [medley.core :refer [find-first]]
            [nextjournal.clerk :as clerk]
            [adventofcode.util :as util]))

{::clerk/visibility {:result :hide}}

^{::clerk/no-cache true}
(def example-input (slurp "datasets/day-2/example-input.txt"))

^{::clerk/no-cache true}
(def puzzle-input (util/slurp-if-exists "datasets/day-2/input.txt"))

;; ## Part 1

;; ### Solution

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

;; ### Example input

^{::clerk/visibility {:result :show}}
(def safe (safe-reports (parse-levels example-input)))

^{::clerk/visibility {:result :show}}
(count safe)

;; ### Input

^{::clerk/visibility {:result :show}}
(some-> (util/not-blank puzzle-input)
        parse-levels
        safe-reports
        count)

;; ## Part 2

;; ### Solution

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

;; ### Example input

^{::clerk/visibility {:result :show}}
(def safe-v2 (safe-reports-v2 (parse-levels example-input)))

^{::clerk/visibility {:result :show}}
(count safe-v2)

;; ### Input

^{::clerk/visibility {:result :show}}
(some-> (util/not-blank puzzle-input)
        parse-levels
        safe-reports-v2
        count)
