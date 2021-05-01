(ns test-runner.core-test
  (:require [clojure.test :refer :all]
            [test-runner.core :refer :all]))

(def problem-dir "fixtures/problems")
(defn problem-rel-path [rel-path] (str problem-dir "/" rel-path))

(defn is-valid-command [[cmd script-name lang symbol path]]
  (and (= cmd "bash")
       (= script-name "test.sh")
       (= symbol :dir)
       (= path (problem-rel-path "hello-world/_test"))))

(defn fake-sh--all-pass [& command]
  (if (is-valid-command command)
    {:exit 0 :err ""}
    {:exit 1 :err "INVALID COMMAND"}))
(defn fake-sh--all-fail [_ _ lang & _]
  {:exit 1 :err (str lang " test failed.")})

(deftest test
  (testing "Test pass if all language tests passed"
    (let [result ((test-all problem-dir) fake-sh--all-pass)]
      (is (= result {:exit 0 :err ""}))))

  (testing "Accumulates exit status of all test commands"
    (let [result ((test-all problem-dir) fake-sh--all-fail)]
      (is (= result {:exit 2 :err "clojure test failed.\n\nbash test failed."}))))
  )
