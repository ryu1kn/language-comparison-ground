# Avoid dependency fetch message going into the output
clojure -Stree > /dev/null

clojure main.clj "$1"
