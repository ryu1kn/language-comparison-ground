(ns test-runner.core
  (:require [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [clojure.string :as s])
  (:gen-class))

(def test-dir "/_test")
(defn dirname [path] (-> path (io/file) (.getParent)))
(defn basename [path] (-> path (io/file) (.getName)))

(defn list-files [dirpath]
  (map #(str dirpath "/" %) (seq (.list (io/file dirpath)))))

(defn target-dirs [dirpath]
  (remove #(s/ends-with? % test-dir) (mapcat list-files (list-files dirpath))))

(defn test-dir-path [lang-dir] (-> lang-dir (dirname) (str test-dir)))

(defn run-test [problem-root-dir]
  (let [solution-dir (basename problem-root-dir)]
    ["bash" "test.sh" solution-dir :dir (test-dir-path problem-root-dir)]))

(defn test-all [problem-dir]
  (fn [sh]
    (let [lang-dirs (target-dirs problem-dir)
          commands (map run-test lang-dirs)]
      (map #(apply sh %) commands))))

(defn -main [& args]
  (let [test-runner (test-all "fixtures/problems")]
    (do (doall (test-runner sh))
        (shutdown-agents))))
