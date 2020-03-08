(ns test.core-test
  (:require [clojure.test :refer :all]
            [test.core :refer :all]
            [clojure.java.shell :refer [with-sh-dir sh]]))

(defn exec-test [dirpath sh-cmd]
  (with-sh-dir (str "../topics/" dirpath) (sh "bash" "-c" sh-cmd)))

;(defn list-files [dirpath]
;  (map #(str dirpath "/" %) (seq (.list (clojure.java.io/file dirpath)))))
;(println (list-files "../topics/hello-world"))

(deftest hello-world
  (testing "Hello World - sample"
    (is (= "Hello World!\n" (:out (exec-test "hello-world/sample" "bash run.sh"))))))
