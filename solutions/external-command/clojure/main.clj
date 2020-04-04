(use '[clojure.java.shell :only [sh]])

(print (:out (sh "echo" "Hello World!")))

; XXX: https://clojureverse.org/t/why-doesnt-my-program-exit/3754
(shutdown-agents)
