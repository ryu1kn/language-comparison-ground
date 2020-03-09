(ns test.core-test
  (:require [clojure.test :refer :all]
            [test.core :refer :all]
            [clojure.java.shell :refer [with-sh-dir sh]]))

(defn exec-test [dirpath sh-cmd]
  (:out (with-sh-dir dirpath (sh "bash" "-c" sh-cmd))))

(defn list-files [dirpath]
  (map #(str dirpath "/" %) (seq (.list (clojure.java.io/file dirpath)))))

(defn def-sh-test [basename cmd expected dirpath]
  `(deftest ~(gensym basename)
     (testing "dummy name"
       (is (= ~expected (exec-test ~dirpath ~cmd))))))

(defmacro def-test-for-all [basename cmd expected files]
  `(do ~@(map #(def-sh-test basename cmd expected %) (eval files))))

(def-test-for-all hello-world "bash run.sh" "Hello World!\n" (list-files "../topics/hello-world"))
