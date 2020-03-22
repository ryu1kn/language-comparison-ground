(ns test.core-test
  (:require [clojure.test :refer :all]
            [test.core :refer :all]
            [clojure.java.shell :refer [with-sh-dir sh]]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(def proj-root-path (System/getProperty "user.dir"))

(defn exec-test [dirpath sh-cmd]
  (:out (with-sh-dir dirpath (apply sh sh-cmd))))

(defn sh' [& args]
  (let [[cmd opt-list] (split-with string? args)
        opt-map (apply hash-map opt-list)]
    (apply sh
      (concat
        cmd
        [:dir (:dir opt-map)]
        [:env (into {} [(System/getenv) (:env opt-map)])]))))

(defn list-files [dirpath]
  (map #(str dirpath "/" %) (seq (.list (clojure.java.io/file dirpath)))))

(defn abs-path [relpath] (str proj-root-path "/" relpath))

(defn lang-name [dirpath] (last (str/split dirpath #"/")))

(io/make-parents (str proj-root-path "tmp/whatever"))

(deftest hello-world
  (doseq [testdir (list-files "../topics/hello-world")]
    (testing (lang-name testdir)
      (is (= "Hello World!\n" (exec-test testdir ["bash" "run.sh"]))))))

(deftest file-io
  (doseq [testdir (list-files "../topics/file-io")]
    (let [out-file (abs-path (str "tmp/file-io-output-" (lang-name testdir) ".txt"))
          in-file (abs-path "file-io-input.txt")]
      (testing (lang-name testdir)
        (is (= "Hello World!\n"
          (do
            (exec-test testdir ["bash" "run.sh" in-file out-file])
            (slurp out-file))))))))

(deftest external-command
  (doseq [testdir (list-files "../topics/external-command")]
    (testing (lang-name testdir)
      (is (= "Hello World!\n" (exec-test testdir ["bash" "run.sh"]))))))

(deftest json
  (doseq [testdir (list-files "../topics/json")]
    (let [in-file (abs-path "json-input.json")
          expected-out-file (abs-path "json-output.json")]
      (testing (lang-name testdir)
        (is (= (slurp expected-out-file) (exec-test testdir ["bash" "run.sh" in-file])))))))

(deftest environment-variable
  (doseq [testdir (list-files "../topics/environment-variable")]
    (testing (lang-name testdir)
      (is (= "Hello World!\n" (:out (sh' "bash" "run.sh" :dir testdir :env {"TEST_MESSAGE" "Hello"})))))))
