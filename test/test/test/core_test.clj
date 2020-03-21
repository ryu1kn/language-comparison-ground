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

(defn make-command [& parts] (str/join " " parts))

(defn abs-path [relpath] (str proj-root-path "/" relpath))

(defn lang-name [dirpath] (last (str/split dirpath #"/")))

(io/make-parents (str proj-root-path "tmp/whatever"))

(deftest hello-world
  (doseq [testdir (list-files "../topics/hello-world")]
    (testing (lang-name testdir)
      (is (= "Hello World!\n" (exec-test testdir "bash run.sh"))))))

(deftest file-io
  (doseq [testdir (list-files "../topics/file-io")]
    (let [out-file (abs-path (str "tmp/file-io-output-" (lang-name testdir) ".txt"))
          in-file (abs-path "file-io-input.txt")]
      (testing (lang-name testdir)
        (is (= "Hello World!\n"
          (do
            (exec-test testdir (make-command "bash" "run.sh" in-file out-file))
            (slurp out-file))))))))
