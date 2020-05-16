(ns test-runner.core
  (:require [clojure.java.io :as io]
            [clojure.string :as s])
  (:gen-class))

(defn dirname [path] (-> path (io/file) (.getParent)))
(defn basename [path] (-> path (io/file) (.getName)))

(defn list-files [dirpath]
  (map #(str dirpath "/" %) (seq (.list (io/file dirpath)))))

(defn target-dirs [dirpath]
  (mapcat list-files (list-files dirpath)))

(defn test-dir [lang-dir] (-> lang-dir (dirname) (str "/_test")))

(defn run-test [problem-root-dir]
  (let [dir1 (test-dir problem-root-dir)
        dir2 (basename problem-root-dir)]
    ["bash" "test.sh" dir2 :dir dir1]))

(defn test-all [problem-dir]
  (fn [sh]
    (let [lang-dirs (target-dirs problem-dir)
          commands (map run-test lang-dirs)]
      (apply sh commands))))

(defn -main [& args] (println "Hello, World!"))
