(ns test.core-test
  (:require [clojure.test :refer :all]
            [test.core :refer :all]
            [clojure.java.shell :refer [with-sh-dir sh]]
            [clojure.string :as str]))

(defn exec-test [dirpath sh-cmd]
  (:out (with-sh-dir dirpath (sh "bash" "-c" sh-cmd))))

(defn list-files [dirpath]
  (map #(str dirpath "/" %) (seq (.list (clojure.java.io/file dirpath)))))

(defn lang-name [dirpath] (last (str/split dirpath #"/")))

(deftest hello-world
  (doseq [file (list-files "../topics/hello-world")]
    (testing (lang-name file)
      (is (= "Hello World!\n" (exec-test file "bash run.sh"))))))
