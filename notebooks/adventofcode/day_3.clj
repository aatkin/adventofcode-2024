^{:nextjournal.clerk/visibility {:code :hide}}
(ns adventofcode.day-3
  {:nextjournal.clerk/toc true}
  (:require [clojure.string :as str]
            [nextjournal.clerk :as clerk]
            [adventofcode.util :as util]))

{::clerk/visibility {:result :hide}}

;; ## Part 1

^{::clerk/no-cache true}
(def example-input (slurp "datasets/day-3/example-input.txt"))

^{::clerk/no-cache true}
(def puzzle-input (util/slurp-if-exists "datasets/day-3/input.txt"))

;; ### Solution

(def +mul-regex+ #"mul\((\d{1,3}),(\d{1,3})\)")

(defn parse-mul [input]
  (for [[match x y] (re-seq +mul-regex+ input)]
    [match (* (parse-long x)
              (parse-long y))]))

(defn mul-sum [input]
  (->> (parse-mul input)
       (map second)
       (reduce + 0)))

;; ### Example input
{::clerk/visibility {:result :show}}

(parse-mul example-input)

(mul-sum example-input)

;; ### Input

(some-> (util/not-blank puzzle-input)
        parse-mul)

(some-> (util/not-blank puzzle-input)
        mul-sum)

{::clerk/visibility {:result :hide}}
;; ## Part 2

;; ### Solution

(defn find-indexes [input s]
  (loop [res []]
    (if-let [match (str/index-of input s (inc (or (last res) -1)))]
      (recur (conj res match))
      res)))

(defn indexed-muls [input]
  (for [[match sum] (parse-mul input)]
    [(find-indexes input match) match sum]))

(defn enabled-ranges [input]
  (loop [sorted (->> (concat (map vector (find-indexes input "do()") (repeat :do))
                             (map vector (find-indexes input "don't()") (repeat :dont)))
                     (sort-by first))
         enabled? true
         ranges [0]] ; start of input initially

    (if (empty? sorted)
      (cond-> ranges
        enabled? (conj (count input)) ; end of input
        true (->> (partition-all 2)))

      (let [[r conditional] (first sorted)]
        (case conditional
          :do (recur (rest sorted)
                     true
                     (cond-> ranges (not enabled?) (conj (+ r 4))))

          :dont (recur (rest sorted)
                       false
                       (cond-> ranges enabled? (conj (dec r)))))))))

(defn enabled-muls [input]
  (let [ranges (enabled-ranges input)
        enabled-mul? (fn [index]
                       (some (fn [[start end]] (<= start index end)) ranges))]
    (->> (indexed-muls input)
         (eduction (map (fn [[indexes match sum]]
                          [(filter enabled-mul? indexes) match sum]))
                   (filter (comp seq first)))
         (mapv rest))))

;; ### Example input
{::clerk/visibility {:result :show}}

(enabled-muls example-input)

(->> (enabled-muls example-input)
     (map second)
     (reduce + 0))

;; ### Input

(some-> (util/not-blank puzzle-input)
        enabled-muls)

(some->> (util/not-blank puzzle-input)
         enabled-muls
         (map second)
         (reduce + 0))

{::clerk/visibility {:result :hide}}
