(ns test-runner.core
  (:require [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [clojure.string :as s])
  (:gen-class))

(def test-dir "/_test")
(defn dirname [path] (-> path (io/file) (.getParent)))
(defn basename [path] (-> path (io/file) (.getName)))
(defn sys-exit [status] (System/exit status))

(defn list-files [dirpath]
  (map #(str dirpath "/" %) (seq (.list (io/file dirpath)))))

(defn target-dirs [dirpath]
  (remove #(s/ends-with? % test-dir) (mapcat list-files (list-files dirpath))))

(defn test-dir-path [lang-dir] (-> lang-dir (dirname) (str test-dir)))

(defn run-test [problem-root-dir]
  (let [solution-dir (basename problem-root-dir)]
    ["bash" "test.sh" solution-dir :dir (test-dir-path problem-root-dir)]))

(defn add-sh-out [out1 out2]
  (let [{exit-1 :exit err-1 :err} out1
        {exit-2 :exit err-2 :err} out2]
    {:exit (+ exit-1 exit-2)
     :err (->> [err-1 err-2] (remove empty?) (s/join "\n\n"))}))

(defn aggregate-result [sh-outs] (reduce add-sh-out {:exit 0 :err ""} sh-outs))

(defn test-all [problem-dir]
  (fn [sh]
    (let [lang-dirs (target-dirs problem-dir)
          commands (map run-test lang-dirs)]
      (aggregate-result (map #(apply sh %) commands)))))

(defn -main [problems-root]
  (let [test-runner (test-all problems-root)]
    (do (println (test-runner sh))
        (shutdown-agents))))
