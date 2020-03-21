(ns test.core-test
  (:require [clojure.test :refer :all]
            [test.core :refer :all]
            [clojure.java.shell :refer [with-sh-dir sh]]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(def proj-root-path (System/getProperty "user.dir"))

(defn exec-test [dirpath sh-cmd]
  (:out (with-sh-dir dirpath (sh "bash" "-c" sh-cmd))))

(defn list-files [dirpath]
  (map #(str dirpath "/" %) (seq (.list (clojure.java.io/file dirpath)))))

(defn lang-name [dirpath] (last (str/split dirpath #"/")))

(io/make-parents (str proj-root-path "tmp/whatever"))

(deftest hello-world
  (doseq [file (list-files "../topics/hello-world")]
    (testing (lang-name file)
      (is (= "Hello World!\n" (exec-test file "bash run.sh"))))))

(deftest file-io
  (doseq [file (list-files "../topics/file-io")
          out-file [(str proj-root-path "/tmp/file-io-output-" (lang-name file) ".txt")]]
    (testing (lang-name file)
      (is (= "Hello World!\n" (do
        (exec-test file (str "bash run.sh" " " (.getAbsolutePath (io/file "file-io-input.txt")) " " out-file))
        (slurp out-file)))))))
