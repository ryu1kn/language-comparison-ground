(ns test-runner.core-test
  (:require [clojure.test :refer :all]
            [test-runner.core :refer :all]))

(def problem-dir "problems")
(defn fake-sh [& command] [command])

(deftest test
  (testing "Find language directories"
    (is (= '("problems/hello-world/bash") (target-dirs problem-dir))))

  (testing "Find test runner directories"
    (is (= "problems/hello-world/_test" (test-dir "problems/hello-world/bash"))))

  (testing "Run a test against a language directory found"
    (let [commands ((test-all problem-dir) fake-sh)]
      (is (= commands [["bash" "test.sh" "clojure" :dir "problems/hello-world/_test"]]))))
  )
