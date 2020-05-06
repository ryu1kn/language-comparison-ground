(ns test-runner.core
  (:require [clojure.java.io :as io])
  (:gen-class))

(defn target-dirs [dirpath]
  (map #(str dirpath "/" %) (seq (.list (io/file dirpath)))))

(defn -main [& args] (println "Hello, World!"))
