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

  (testing "Programme choose 1 for exit status when any test failed"
    (let [shell-outputs [{:exit 0} {:exit 1}]]
      (is (= (aggregate-result shell-outputs) {:exit 1}))))

  ;(testing "Programme exits with 1 when any test failed"
  ;  (let [runner (test-all problem-dir)
  ;        count (atom -1)
  ;        fake-sh (fn [& commands] (do {:exit (swap! count #(+ % 1))}))
  ;        exit-call identity]
  ;    (is (= (runner fake-sh) 1))))
  )
