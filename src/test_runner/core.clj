(ns test-runner.core
  (:require [clojure.java.io :as io]
            [clojure.string :as s])
  (:gen-class))

(defn list-files [dirpath]
  (map #(str dirpath "/" %) (seq (.list (io/file dirpath)))))

(defn target-dirs [dirpath]
  (mapcat list-files (list-files dirpath)))

(defn test-dir [lang-dir]
  (-> lang-dir (s/split #"/") (drop-last) (concat ["_test"]) (#(s/join "/" %))))

(defn test-all [problem-dir]
  (fn [sh]
    (let [command ["bash" "test.sh" "clojure" :dir "problems/hello-world/_test"]]
      (apply sh command))))

(defn -main [& args] (println "Hello, World!"))
