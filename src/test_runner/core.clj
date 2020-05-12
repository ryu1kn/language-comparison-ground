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
  (fn [sh] [{:dir "problems/hello-world/_test" :cmd "bash test.sh clojure"}]))

(defn -main [& args] (println "Hello, World!"))
