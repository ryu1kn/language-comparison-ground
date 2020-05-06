(ns test-runner.core-test
  (:require [clojure.test :refer :all]
            [test-runner.core :refer :all]))

(def problem-dir "problems")

(deftest a-test
  (testing "Find problem directories"
    (is (= '("problems/hello-world") (target-dirs problem-dir)))))
