(ns test-runner.core
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as s]
            [test-runner.file :refer [dirname basename list-files]])
  (:gen-class))

(def test-dir "/_test")
(defn sys-exit [status] (System/exit status))

(defn collect-lang-dir-paths [dirpath]
  (remove #(s/ends-with? % test-dir) (mapcat list-files (list-files dirpath))))

(defn test-dir-path [lang-dir] (-> lang-dir (dirname) (str test-dir)))

(defn build-test-command [lang-dir-path]
  (let [lang-dir-name (basename lang-dir-path)]
    ["bash" "test.sh" lang-dir-name :dir (test-dir-path lang-dir-path)]))

(defn add-sh-out
  [{exit-1 :exit err-1 :err}
   {exit-2 :exit err-2 :err}]
  {:exit (+ exit-1 exit-2)
   :err (->> [err-1 err-2] (remove empty?) (s/join "\n\n"))})

(defn aggregate-result [sh-outs] (reduce add-sh-out {:exit 0 :err ""} sh-outs))

(defn test-all [problem-dir]
  (fn [sh]
    (let [lang-dir-paths (collect-lang-dir-paths problem-dir)
          commands (map build-test-command lang-dir-paths)]
      (aggregate-result (map #(apply sh %) commands)))))

(defn finish-test [{:keys [exit err]}]
  (do (println err) (sys-exit exit)))

(defn -main [problems-root]
  (let [test-runner (test-all problems-root)]
    (finish-test (test-runner sh))))
