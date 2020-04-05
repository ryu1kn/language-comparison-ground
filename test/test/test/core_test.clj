(ns test.core-test
  (:require [clojure.test :refer :all]
            [test.core :refer :all]
            [clojure.java.shell :refer [with-sh-dir sh]]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(def proj-root-path (System/getProperty "user.dir"))

(defn sh' [& args]
  (let [[cmd opt-list] (split-with string? args)
        opt-map (apply hash-map opt-list)]
    (apply sh
      (concat
        cmd
        [:dir (:dir opt-map)]
        [:env (into {} [(System/getenv) (:env opt-map)])]))))

(defn sh-out [& args]
  (let [result (apply sh' args)]
    (if (= 0 (:exit result)) result (throw (Exception. (:err result))))))

(defn list-files [dirpath]
  (map #(str dirpath "/" %) (seq (.list (clojure.java.io/file dirpath)))))

(defn abs-path [relpath] (str proj-root-path "/" relpath))

(defn lang-name [dirpath] (last (str/split dirpath #"/")))

(io/make-parents (abs-path "tmp/whatever"))

(deftest hello-world
  (doseq [testdir (list-files "../solutions/hello-world")]
    (testing (lang-name testdir)
      (is (= "Hello World!\n" (:out (sh-out "bash" "run.sh" :dir testdir )))))))

(deftest file-io
  (doseq [testdir (list-files "../solutions/file-io")]
    (let [out-file (abs-path (str "tmp/file-io-output-" (lang-name testdir) ".txt"))
          in-file (abs-path "file-io-input.txt")]
      (testing (lang-name testdir)
        (is (= "Hello World!\n"
          (do
            (:out (sh-out "bash" "run.sh" in-file out-file :dir testdir))
            (slurp out-file))))))))

(deftest external-command
  (doseq [testdir (list-files "../solutions/external-command")]
    (testing (lang-name testdir)
      (is (= "Hello World!\n" (:out (sh-out "bash" "run.sh" :dir testdir)))))))

(deftest json
  (doseq [testdir (list-files "../solutions/json")]
    (let [in-file (abs-path "json-input.json")
          expected-out-file (abs-path "json-output.json")]
      (testing (lang-name testdir)
        (is (= (slurp expected-out-file) (:out (sh-out "bash" "run.sh" in-file :dir testdir))))))))

(deftest environment-variable
  (doseq [testdir (list-files "../solutions/environment-variable")]
    (testing (lang-name testdir)
      (is (= "Hello World!\n" (:out (sh-out "bash" "run.sh" :dir testdir :env {"TEST_MESSAGE" "Hello"})))))))
