(ns test-runner.core-test
  (:require [clojure.test :refer :all]
            [test-runner.core :refer :all]))

(def problem-dir "fixtures/problems")
(defn problem-rel-path [rel-path] (str problem-dir "/" rel-path))
(defn fake-sh [& command] command)

(deftest test
  (testing "Run a test against a language directory found"
    (let [commands ((test-all problem-dir) fake-sh)]
      (is (= commands [
        ["bash" "test.sh" "clojure" :dir (problem-rel-path "hello-world/_test")]
        ["bash" "test.sh" "bash" :dir (problem-rel-path "hello-world/_test")]]))))
  )
