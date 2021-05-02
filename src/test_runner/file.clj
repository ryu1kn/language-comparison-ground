(ns test-runner.file
  (:require [clojure.java.io :as io]))

(defn dirname [path] (-> path (io/file) (.getParent)))

(defn basename [path] (-> path (io/file) (.getName)))

(defn list-files [dirpath]
  (map #(str dirpath "/" %) (seq (.list (io/file dirpath)))))
