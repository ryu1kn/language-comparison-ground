(require '[clojure.data.json :as json])

(defn inc-age [people]
  {"people" (map #(merge % {"age" (+ 1 (% "age"))}) (people "people"))})

(let [file-in (first *command-line-args*)]
  (println (json/write-str (inc-age (json/read-str (slurp file-in))))))
